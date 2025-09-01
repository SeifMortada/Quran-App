package com.seifmortada.applications.quran.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seifmortada.applications.quran.MainActivity
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import com.seifmortada.applications.quran.app.CHANNEL_ID
import com.seifmortada.applications.quran.app.BACKUP_CHANNEL_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadService : Service() {

    companion object {
        private const val TAG = "DownloadService"
        private const val NOTIFICATION_ID = 2001
        private const val CHANNEL_NAME = "Quran App"

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
        const val EXTRA_TARGET_ACTIVITY_CLASS = "target_activity_class"

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
        Log.d(TAG, "=== DownloadService onCreate START ===")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        fileManager = QuranFileManager(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        Log.d(TAG, "=== About to create notification channel ===")
        createNotificationChannel()
        Log.d(TAG, "=== DownloadService onCreate COMPLETE ===")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "=== onStartCommand START ===")
        Log.d(TAG, "Intent action: ${intent?.action}")

        when (intent?.action) {
            ACTION_START_DOWNLOAD -> {
                Log.d(TAG, "=== Processing START_DOWNLOAD action ===")
                val downloadUrl = intent.getStringExtra(EXTRA_DOWNLOAD_URL)
                val reciterName = intent.getStringExtra(EXTRA_RECITER_NAME)
                val surahNumber = intent.getIntExtra(EXTRA_SURAH_NUMBER, 0)
                val surahNameAr = intent.getStringExtra(EXTRA_SURAH_NAME_AR)
                val surahNameEn = intent.getStringExtra(EXTRA_SURAH_NAME_EN)
                val serverUrl = intent.getStringExtra(EXTRA_SERVER_URL)
                val targetActivityClass = intent.getStringExtra(EXTRA_TARGET_ACTIVITY_CLASS)

                Log.d(
                    TAG,
                    "Download params - URL: $downloadUrl, Reciter: $reciterName, Surah: $surahNumber"
                )
                Log.d(TAG, "Server URL: $serverUrl, Target Activity: $targetActivityClass")

                if (downloadUrl != null && reciterName != null && serverUrl != null) {
                    Log.d(TAG, "=== All required params present, starting download ===")
                    startDownload(
                        downloadUrl,
                        reciterName,
                        surahNumber,
                        surahNameAr,
                        surahNameEn,
                        serverUrl,
                        targetActivityClass
                    )
                } else {
                    Log.e(TAG, "=== MISSING REQUIRED PARAMS ===")
                    Log.e(
                        TAG,
                        "downloadUrl: $downloadUrl, reciterName: $reciterName, serverUrl: $serverUrl"
                    )
                }
            }

            ACTION_CANCEL_DOWNLOAD -> {
                Log.d(TAG, "=== Processing CANCEL_DOWNLOAD action ===")
                cancelDownload()
            }

            else -> {
                Log.w(TAG, "=== Unknown action: ${intent?.action} ===")
            }
        }

        Log.d(TAG, "=== onStartCommand COMPLETE ===")
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
        serverUrl: String,
        targetActivityClass: String?
    ) {
        Log.d(TAG, "=== startDownload METHOD START ===")
        Log.d(TAG, "startDownload params - Reciter: $reciterName, Surah: $surahNumber")

        // Ensure notification channel exists (should be created by Application)
        Log.d(TAG, "=== About to ensure notification channel exists ===")
        createNotificationChannel()

        Log.d(TAG, "=== Notification channel check complete ===")

        // Check if file already exists
        Log.d(TAG, "=== Checking if file already exists ===")
        if (fileManager.surahFileExists(
                reciterName,
                serverUrl,
                surahNumber,
                surahNameAr,
                surahNameEn
            )
        ) {
            Log.d(TAG, "=== File already exists, broadcasting completion ===")
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

        Log.d(TAG, "=== File doesn't exist, proceeding with download ===")

        val outputFile = fileManager.getSurahFilePath(
            reciterName,
            serverUrl,
            surahNumber,
            surahNameAr,
            surahNameEn
        )
        val displayName = surahNameEn ?: "Surah $surahNumber"

        Log.d(TAG, "=== Output file path: ${outputFile.absolutePath} ===")
        Log.d(TAG, "=== Display name: $displayName ===")

        // Create notification before starting foreground
        Log.d(TAG, "=== CRITICAL: About to create progress notification ===")

        // CRITICAL: Final check of channel importance before startForeground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val finalChannelCheck = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (finalChannelCheck != null) {
                Log.d(
                    TAG,
                    "=== FINAL CHANNEL CHECK: Channel importance is ${finalChannelCheck.importance} ==="
                )
                if (finalChannelCheck.importance < NotificationManager.IMPORTANCE_DEFAULT) {
                    Log.e(
                        TAG,
                        "=== CRITICAL ERROR: Channel importance is too low for foreground service! ==="
                    )
                    Log.e(
                        TAG,
                        "Channel importance: ${finalChannelCheck.importance}, Minimum required: ${NotificationManager.IMPORTANCE_DEFAULT}"
                    )
                    Log.e(TAG, "This will cause startForeground to fail on Android 13+!")
                    Log.e(
                        TAG,
                        "=== User guidance: Please check notification settings and set Quran App's channel importance to 'Default' or higher ==="
                    )

                    // Use BACKUP_CHANNEL_ID which will be correct
                    val backupChannel =
                        notificationManager.getNotificationChannel(BACKUP_CHANNEL_ID)
                    if (backupChannel == null) {
                        try {
                            val c = NotificationChannel(
                                BACKUP_CHANNEL_ID,
                                CHANNEL_NAME + " (Backup)",
                                NotificationManager.IMPORTANCE_DEFAULT
                            )
                            notificationManager.createNotificationChannel(c)
                            Log.i(TAG, "=== Created BACKUP notification channel ===")
                        } catch (e: Exception) {
                            Log.e(TAG, "Error creating BACKUP_CHANNEL_ID", e)
                        }
                    } else {
                        Log.d(TAG, "=== BACKUP_CHANNEL_ID exists & will be used ===")
                    }
                }
            }
        }

        try {
            val initialNotification = createProgressNotification(
                displayName,
                0,
                reciterName,
                surahNumber
            )
            Log.d(TAG, "=== CRITICAL: Progress notification created successfully ===")

            Log.d(TAG, "=== CRITICAL: About to call startForeground ===")
            Log.d(TAG, "=== CRITICAL: Channel ID being used: $CHANNEL_ID ===")
            Log.d(TAG, "=== CRITICAL: Notification ID: $NOTIFICATION_ID ===")
            Log.d(TAG, "=== CRITICAL: Android API Level: ${Build.VERSION.SDK_INT} ===")

            // Start foreground with notification - Android 13+ compatible
            var channelIdToUse = CHANNEL_ID
            // If importance issue detected (see previous block), use BACKUP_CHANNEL_ID
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val finalCheck = notificationManager.getNotificationChannel(CHANNEL_ID)
                if (finalCheck != null && finalCheck.importance < NotificationManager.IMPORTANCE_DEFAULT) {
                    channelIdToUse = BACKUP_CHANNEL_ID
                    Log.w(TAG, "Switching to BACKUP_CHANNEL_ID for foreground notification!")
                }
            }

            val initialNotificationToUse: Notification = if (channelIdToUse == CHANNEL_ID) {
                initialNotification
            } else {
                createProgressNotification(
                    displayName,
                    0,
                    reciterName,
                    surahNumber,
                    channelIdOverride = channelIdToUse
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Log.d(TAG, "=== CRITICAL: Using Android 14+ startForeground with service type ===")
                startForeground(
                    NOTIFICATION_ID,
                    initialNotificationToUse,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                Log.d(TAG, "=== CRITICAL: Using standard startForeground ===")
                startForeground(NOTIFICATION_ID, initialNotificationToUse)
            }
            Log.d(TAG, "=== CRITICAL: startForeground completed successfully! ===")

        } catch (e: Exception) {
            Log.e(TAG, "=== CRITICAL: EXCEPTION during notification/startForeground ===", e)
            Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
            Log.e(TAG, "Exception message: ${e.message}")

            // Additional debugging for Android 13+ notification issues
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val debugChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
                if (debugChannel != null) {
                    Log.e(TAG, "=== DEBUG: Final channel state ===")
                    Log.e(TAG, "Channel ID: ${debugChannel.id}")
                    Log.e(TAG, "Channel name: ${debugChannel.name}")
                    Log.e(TAG, "Channel importance: ${debugChannel.importance}")
                    Log.e(TAG, "Channel canBypassDnd: ${debugChannel.canBypassDnd()}")
                    Log.e(TAG, "Channel shouldShowLights: ${debugChannel.shouldShowLights()}")
                }
            }

            throw e  // Re-throw to see full stack trace
        }

        Log.d(TAG, "=== About to start download job ===")
        currentDownloadJob = serviceScope.launch {
            try {
                downloadFile(
                    downloadUrl,
                    outputFile,
                    displayName,
                    reciterName,
                    surahNumber
                )
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

        Log.d(TAG, "=== startDownload METHOD COMPLETE ===")
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
                    val channelIdToUse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val ch = notificationManager.getNotificationChannel(CHANNEL_ID)
                        if (ch != null && ch.importance < NotificationManager.IMPORTANCE_DEFAULT) {
                            BACKUP_CHANNEL_ID
                        } else CHANNEL_ID
                    } else CHANNEL_ID
                    val notification = createProgressNotification(
                        displayName,
                        progressPercent,
                        reciterName,
                        surahNumber,
                        channelIdOverride = channelIdToUse
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
        Log.d(TAG, "=== createNotificationChannel START ===")
        Log.d(TAG, "Android API Level: ${Build.VERSION.SDK_INT}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "=== API >= O, creating notification channel ===")
            Log.d(TAG, "Target Channel ID: $CHANNEL_ID")

            // Channel should already be created by Application class
            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel != null) {
                Log.d(TAG, "=== Using existing notification channel: $CHANNEL_ID ===")
                Log.d(TAG, "Existing channel name: ${existingChannel.name}")
                Log.d(TAG, "Existing channel importance: ${existingChannel.importance}")
                Log.d(
                    TAG,
                    "Required importance (IMPORTANCE_DEFAULT): ${NotificationManager.IMPORTANCE_DEFAULT}"
                )

                if (existingChannel.importance != NotificationManager.IMPORTANCE_DEFAULT) {
                    Log.w(TAG, "=== WARNING: Existing channel has wrong importance! ===")
                    Log.w(
                        TAG,
                        "Channel importance: ${existingChannel.importance}, Required: ${NotificationManager.IMPORTANCE_DEFAULT}"
                    )
                }
            } else {
                Log.w(TAG, "=== Notification channel $CHANNEL_ID not found, creating fallback ===")
                try {
                    // Create fallback channel with correct Android 13+ settings
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT  // FIXED: Android 13+ requires IMPORTANCE_DEFAULT
                    ).apply {
                        description = "Notifications for Quran app services"
                        setSound(null, null)
                        enableVibration(false)
                        enableLights(false)
                        setShowBadge(false)
                    }
                    Log.d(TAG, "=== About to create fallback channel ===")
                    notificationManager.createNotificationChannel(channel)
                    Log.d(TAG, "=== Created fallback notification channel: $CHANNEL_ID ===")

                    // Verify creation
                    val verifyChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
                    if (verifyChannel != null) {
                        Log.d(TAG, "=== Verification: Channel created successfully ===")
                        Log.d(TAG, "Verified channel importance: ${verifyChannel.importance}")
                    } else {
                        Log.e(
                            TAG,
                            "=== ERROR: Channel creation failed - verification returned null ==="
                        )
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "=== CRITICAL: Exception creating notification channel ===", e)
                    throw e
                }
            }
        } else {
            Log.d(TAG, "=== API < O, no channel creation needed ===")
        }

        Log.d(TAG, "=== createNotificationChannel COMPLETE ===")
    }

    private fun createProgressNotification(
        displayName: String,
        progress: Int,
        reciterName: String,
        surahNumber: Int,
        channelIdOverride: String? = null
    ): Notification {
        val channelId = channelIdOverride ?: CHANNEL_ID
        Log.d(TAG, "=== createProgressNotification START ===")
        Log.d(
            TAG,
            "Notification params - Display: $displayName, Progress: $progress, Reciter: $reciterName, Surah: $surahNumber"
        )
        Log.d(TAG, "Using Channel ID: $channelId")

        try {
            Log.d(TAG, "=== Creating MainActivity intent ===")
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            Log.d(TAG, "=== MainActivity PendingIntent created successfully ===")

            Log.d(TAG, "=== Creating cancel intent ===")
            val cancelIntent = Intent(this, DownloadService::class.java).apply {
                action = ACTION_CANCEL_DOWNLOAD
            }
            val cancelPendingIntent = PendingIntent.getService(
                this, 0, cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            Log.d(TAG, "=== Cancel PendingIntent created successfully ===")

            Log.d(TAG, "=== Building notification ===")
            Log.d(TAG, "Channel ID: $channelId")
            Log.d(TAG, "Priority: ${NotificationCompat.PRIORITY_DEFAULT}")
            Log.d(TAG, "Small icon: android.R.drawable.stat_sys_download")

            val builder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Downloading $reciterName - $displayName")
                .setContentText("$progress% complete")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setProgress(100, progress, false)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // FIXED: Android 13+ compatible
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentIntent(pendingIntent)
                .addAction(
                    android.R.drawable.ic_menu_close_clear_cancel,
                    "Cancel",
                    cancelPendingIntent
                )

            // Add guidance if NOT using main channel (i.e. backup channel)
            if (channelId != CHANNEL_ID) {
                builder.setSubText("Notification settings issue detected. Please set Quran App notifications to 'Default' importance in system settings for proper downloads.")
            }

            val notification = builder.build()

            Log.d(TAG, "=== Notification built successfully ===")
            Log.d(TAG, "=== createProgressNotification COMPLETE ===")
            return notification

        } catch (e: Exception) {
            Log.e(TAG, "=== CRITICAL: Exception in createProgressNotification ===", e)
            Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
            Log.e(TAG, "Exception message: ${e.message}")
            throw e
        }
    }

    private fun updateNotificationCompleted(
        displayName: String,
        reciterName: String,
        surahNumber: Int
    ) {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (ch != null && ch.importance < NotificationManager.IMPORTANCE_DEFAULT) {
                BACKUP_CHANNEL_ID
            } else CHANNEL_ID
        } else CHANNEL_ID

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Download completed")
            .setContentText("$reciterName - $displayName")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun updateNotificationError(
        displayName: String,
        reciterName: String,
        surahNumber: Int
    ) {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (ch != null && ch.importance < NotificationManager.IMPORTANCE_DEFAULT) {
                BACKUP_CHANNEL_ID
            } else CHANNEL_ID
        } else CHANNEL_ID

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Download failed")
            .setContentText("$reciterName - $displayName")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}