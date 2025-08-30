package com.seifmortada.applications.quran.core.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.app.CHANNEL_ID
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.Audio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val REWIND = "rewind"
const val FORWARD = "forward"
const val PLAYPAUSE = "play-pause"
const val SEEK_TO = "seek-to"
const val AUDIO_LOAD = "audio-load"

class AudioPlayerService : Service() {

    val binder = AudioPlayerBinder()

    override fun onCreate() {
        super.onCreate()
        val defaultNotification = createDefaultNotification()
        startForeground(1, defaultNotification)
    }

    private fun createDefaultNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Quran App")
            .setContentText("Preparing audio...")
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.quran_app_logo))
            .build()
    }

    inner class AudioPlayerBinder : Binder() {
        fun getService() = this@AudioPlayerService
    }

    private var mediaPlayer = MediaPlayer()

    val currentAudio = MutableStateFlow(Audio("", "", 0, "", ""))
    val maxDuration = MutableStateFlow(0)
    val currentDuration = MutableStateFlow(0)
    val isPlaying = MutableStateFlow(false)

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                REWIND -> mediaPlayer.seekTo((mediaPlayer.currentPosition - 5000).coerceAtLeast(0))
                FORWARD -> mediaPlayer.seekTo(
                    (mediaPlayer.currentPosition + 5000).coerceAtMost(
                        mediaPlayer.duration
                    )
                )

                PLAYPAUSE -> togglePlayPause()
                SEEK_TO -> {
                    val pos = intent.getIntExtra("SEEK_POSITION", 0)
                    mediaPlayer.seekTo(pos)
                }

                AUDIO_LOAD -> {
                    val path = intent.getStringExtra("AUDIO_PATH")
                    val title = intent.getStringExtra("AUDIO_TITLE") ?: "Unknown Title"
                    val reciterName = intent.getStringExtra("AUDIO_RECITER") ?: "Unknown Reciter"
                    val surahInfo = intent.getStringExtra("AUDIO_SURAH_INFO") ?: ""

                    if (!path.isNullOrEmpty()) prepareAndPlay(path, title, reciterName, surahInfo)
                }

                else -> {
                    val audio = currentAudio.value
                    prepareAndPlay(audio.path, audio.title, audio.reciterName, audio.surahInfo)
                }
            }
        }

        if (currentAudio.value.path.isNotEmpty()) {
            sendNotification(currentAudio.value)
        }

        return START_STICKY
    }

    private fun togglePlayPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying.value = false
        } else {
            mediaPlayer.start()
            isPlaying.value = true
            updateDuration()
        }
        sendNotification(currentAudio.value)
    }

    private fun prepareAndPlay(
        path: String,
        title: String = "",
        reciterName: String = "",
        surahInfo: String = ""
    ) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                val audio = Audio(
                    path = path,
                    title = title,
                    duration = it.duration,
                    reciterName = reciterName,
                    surahInfo = surahInfo
                )
                currentAudio.value = audio
                maxDuration.value = it.duration
                it.start()
                isPlaying.value = true
                sendNotification(currentAudio.value)
                updateDuration()
            }
            mediaPlayer.setOnCompletionListener {
                isPlaying.value = false
                currentDuration.value = 0
                job?.cancel()
                sendNotification(currentAudio.value)
            }
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                isPlaying.value = false
                job?.cancel()
                false
            }
        } catch (e: Exception) {
            isPlaying.value = false
            job?.cancel()
        }
    }

    private fun updateDuration() {
        job?.cancel()
        job = serviceScope.launch {
            while (mediaPlayer.isPlaying) {
                currentDuration.update { mediaPlayer.currentPosition }
                delay(1000)
            }
        }
    }

    private fun sendNotification(audio: Audio) {
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(audio.title)
            .setSubText("${audio.reciterName} - ${audio.surahInfo}")
            .addAction(R.drawable.ic_fast_rewind, "Rewind", createActionIntent(REWIND))
            .addAction(
                if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                if (mediaPlayer.isPlaying) "Pause" else "Play",
                createActionIntent(PLAYPAUSE)
            )
            .addAction(R.drawable.ic_fast_forward, "Forward", createActionIntent(FORWARD))
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.quran_app_logo))
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createActionIntent(action: String): PendingIntent {
        val intent = Intent(this, AudioPlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(), // unique for each action
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun stopPlayback() {
        serviceScope.launch {
            job?.cancel()
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            isPlaying.value = false
            currentDuration.value = 0
            stopForeground(true)
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
    }
}
