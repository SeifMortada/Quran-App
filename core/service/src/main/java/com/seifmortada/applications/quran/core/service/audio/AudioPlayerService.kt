package com.seifmortada.applications.quran.core.service.audio

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.seifmortada.applications.quran.core.service.utils.AUDIO_CHANNEL_ID
import com.seifmortada.applications.quran.core.service.utils.AUDIO_LOAD
import com.seifmortada.applications.quran.core.service.utils.FORWARD
import com.seifmortada.applications.quran.core.service.utils.PLAYPAUSE
import com.seifmortada.applications.quran.core.service.utils.REWIND
import com.seifmortada.applications.quran.core.service.utils.SEEK_TO
import com.seifmortada.applications.quran.core.service.utils.AudioServiceConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AudioPlayerService : Service() {

    val binder = AudioPlayerBinder()

    override fun onCreate() {
        super.onCreate()

        try {
            val defaultNotification = createDefaultNotification()
            startForeground(1, defaultNotification)
        } catch (e: Exception) {
            // Log error but don't expose internal details
            throw RuntimeException("Failed to start audio service", e)
        }
    }

    private fun createDefaultNotification(): Notification {
        return NotificationCompat.Builder(this, AUDIO_CHANNEL_ID)
            .setContentTitle("Quran App")
            .setContentText("Preparing audio...")
            .setSmallIcon(AudioServiceConstants.notificationIcon)
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

        val notification = NotificationCompat.Builder(this, AUDIO_CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(audio.title)
            .setSubText("${audio.reciterName} - ${audio.surahInfo}")
            .addAction(AudioServiceConstants.rewindIcon, "Rewind", createActionIntent(REWIND))
            .addAction(
                if (mediaPlayer.isPlaying) AudioServiceConstants.pauseIcon else AudioServiceConstants.playIcon,
                if (mediaPlayer.isPlaying) "Pause" else "Play",
                createActionIntent(PLAYPAUSE)
            )
            .addAction(AudioServiceConstants.forwardIcon, "Forward", createActionIntent(FORWARD))
            .setSmallIcon(AudioServiceConstants.notificationIcon)
            .build()

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
        serviceScope.cancel()
        stopPlayback()
    }
}
