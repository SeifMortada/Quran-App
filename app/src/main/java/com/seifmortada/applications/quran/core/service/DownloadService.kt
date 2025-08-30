package com.seifmortada.applications.quran.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.storage.QuranFileManager
import com.seifmortada.applications.quran.core.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.BitmapFactory
import kotlinx.coroutines.launch

class DownloadService : Service() {

    companion object {
        private const val TAG = "DownloadService"
        private const val NOTIFICATION_ID = 2001
        private const val CHANNEL_ID = "download_channel"
        private const val CHANNEL_NAME = "Quran Downloads"

        // Intent actions
        const val ACTION_START_DOWNLOAD = "action_start_download"
        const val ACTION_CANCEL_DOWNLOAD = "action_cancel_download"

        // Intent extras
        const val EXTRA_DOWNLOAD_URL = "download_url"
        const val EXTRA_RECITER_NAME = "reciter_name"
        const val EXTRA_SURAH_NUMBER = "surah_number"
        const val EXTRA_SURAH_NAME_AR = "surah_name_ar"
        const val EXTRA_SURAH_NAME_EN = "surah_name_en"
        const val EXTRA_SERVER_URL = "server_url"

        // Broadcast actions
        const val BROADCAST_DOWNLOAD_PROGRESS = "com.seifmortada.quran.DOWNLOAD_PROGRESS"
        const val BROADCAST_DOWNLOAD_COMPLETED = "com.seifmortada.quran.DOWNLOAD_COMPLETED"
        const val BROADCAST_DOWNLOAD_FAILED = "com.seifmortada.quran.DOWNLOAD_FAILED"
        const val BROADCAST_DOWNLOAD_CANCELLED = "com.seifmortada.quran.DOWNLOAD_CANCELLED"

        // Broadcast extras
        const val EXTRA_PROGRESS = "progress"
        const val EXTRA_DOWNLOADED_BYTES = "downloaded_bytes"
        const val EXTRA_TOTAL_BYTES = "total_bytes"
        const val EXTRA_FILE_PATH = "file_path"
        const val EXTRA_ERROR_MESSAGE = "error_message"
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    private lateinit var notificationManager: NotificationManager
    private lateinit var fileManager: QuranFileManager
    private lateinit var localBroadcastManager: LocalBroadcastManager

    private var currentDownloadJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        fileManager = QuranFileManager(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        createNotificationChannel()
        Log.d(TAG, "DownloadService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_DOWNLOAD -> {
                val downloadUrl = intent.getStringExtra(EXTRA_DOWNLOAD_URL)
                val reciterName = intent.getStringExtra(EXTRA_RECITER_NAME)
                val surahNumber = intent.getIntExtra(EXTRA_SURAH_NUMBER, 0)
                val surahNameAr = intent.getStringExtra(EXTRA_SURAH_NAME_AR)
                val surahNameEn = intent.getStringExtra(EXTRA_SURAH_NAME_EN)
                val serverUrl = intent.getStringExtra(EXTRA_SERVER_URL)

                if (downloadUrl != null && reciterName != null && serverUrl != null) {
                    startDownload(
                        downloadUrl,
                        reciterName,
                        surahNumber,
                        surahNameAr,
                        surahNameEn,
                        serverUrl
                    )
                }
            }

            ACTION_CANCEL_DOWNLOAD -> {
                cancelDownload()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        currentDownloadJob?.cancel()
        serviceScope.cancel()
        Log.d(TAG, "DownloadService destroyed")
    }

    private fun startDownload(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?,
        serverUrl: String
    ) {
        // Check if file already exists
        if (fileManager.surahFileExists(
                reciterName,
                serverUrl,
                surahNumber,
                surahNameAr,
                surahNameEn
            )
        ) {
            val existingFile = fileManager.getSurahFilePath(
                reciterName,
                serverUrl,
                surahNumber,
                surahNameAr,
                surahNameEn
            )

            // Broadcast completion
            val intent = Intent(BROADCAST_DOWNLOAD_COMPLETED).apply {
                putExtra(EXTRA_FILE_PATH, existingFile.absolutePath)
            }
            localBroadcastManager.sendBroadcast(intent)

            stopSelf()
            return
        }

        val outputFile = fileManager.getSurahFilePath(
            reciterName,
            serverUrl,
            surahNumber,
            surahNameAr,
            surahNameEn
        )
        val displayName = surahNameEn ?: "Surah $surahNumber"

        // Start foreground with initial notification
        startForeground(
            NOTIFICATION_ID,
            createProgressNotification(displayName, 0, reciterName, surahNumber)
        )

        currentDownloadJob = serviceScope.launch {
            try {
                downloadFile(downloadUrl, outputFile, displayName, reciterName, surahNumber)
            } catch (e: Exception) {
                Log.e(TAG, "Download failed", e)
                updateNotificationError(displayName, reciterName, surahNumber)

                // Broadcast error
                val intent = Intent(BROADCAST_DOWNLOAD_FAILED).apply {
                    putExtra(EXTRA_ERROR_MESSAGE, e.message ?: "Download failed")
                }
                localBroadcastManager.sendBroadcast(intent)

                stopSelf()
            }
        }
    }

    private suspend fun downloadFile(
        url: String,
        outputFile: File,
        displayName: String,
        reciterName: String,
        surahNumber: Int
    ) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val totalBytes = connection.contentLengthLong
        if (totalBytes <= 0) {
            throw Exception("Could not get file size")
        }

        // Ensure parent directory exists
        outputFile.parentFile?.mkdirs()

        val inputStream = connection.inputStream
        val outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        var downloadedBytes = 0L
        var lastProgressBroadcast = 0

        try {
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead

                val progress = downloadedBytes.toFloat() / totalBytes
                val progressPercent = (progress * 100).toInt()

                // Broadcast progress every 5%
                if (progressPercent != lastProgressBroadcast && progressPercent % 5 == 0) {
                    val intent = Intent(BROADCAST_DOWNLOAD_PROGRESS).apply {
                        putExtra(EXTRA_PROGRESS, progress)
                        putExtra(EXTRA_DOWNLOADED_BYTES, downloadedBytes)
                        putExtra(EXTRA_TOTAL_BYTES, totalBytes)
                    }
                    localBroadcastManager.sendBroadcast(intent)
                    lastProgressBroadcast = progressPercent
                }

                // Update notification every 5%
                if (progressPercent % 5 == 0) {
                    val notification = createProgressNotification(
                        displayName,
                        progressPercent,
                        reciterName,
                        surahNumber
                    )
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
            connection.disconnect()

            // Download completed successfully
            updateNotificationCompleted(displayName, reciterName, surahNumber)

            // Broadcast completion
            val intent = Intent(BROADCAST_DOWNLOAD_COMPLETED).apply {
                putExtra(EXTRA_FILE_PATH, outputFile.absolutePath)
                putExtra(EXTRA_PROGRESS, 1f)
                putExtra(EXTRA_DOWNLOADED_BYTES, totalBytes)
                putExtra(EXTRA_TOTAL_BYTES, totalBytes)
            }
            localBroadcastManager.sendBroadcast(intent)

            stopSelf()

        } catch (e: Exception) {
            outputStream.close()
            inputStream.close()
            connection.disconnect()

            // Clean up partially downloaded file
            if (outputFile.exists()) {
                outputFile.delete()
            }
            throw e
        }
    }

    private fun cancelDownload() {
        currentDownloadJob?.cancel()
        val intent = Intent(BROADCAST_DOWNLOAD_CANCELLED)
        localBroadcastManager.sendBroadcast(intent)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows download progress for Quran audio files"
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createProgressNotification(
        displayName: String,
        progress: Int,
        reciterName: String,
        surahNumber: Int
    ): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val cancelIntent = Intent(this, DownloadService::class.java).apply {
            action = ACTION_CANCEL_DOWNLOAD
        }
        val cancelPendingIntent = PendingIntent.getService(
            this, 0, cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(
                getString(
                    R.string.downloading_reciter_surah,
                    reciterName,
                    displayName
                )
            )
            .setContentText("$progress%")
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.quran_app_logo))
            .setProgress(100, progress, false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_settings, // Cancel icon
                getString(R.string.cancel),
                cancelPendingIntent
            )
            .build()
    }

    private fun updateNotificationCompleted(
        displayName: String,
        reciterName: String,
        surahNumber: Int
    ) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(
                getString(
                    R.string.download_completed_reciter_surah,
                    reciterName,
                    displayName
                )
            )
            .setContentText("")
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.quran_app_logo))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun updateNotificationError(
        displayName: String,
        reciterName: String,
        surahNumber: Int
    ) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(
                getString(
                    R.string.download_failed_reciter_surah,
                    reciterName,
                    displayName
                )
            )
            .setContentText("")
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.quran_app_logo))
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}