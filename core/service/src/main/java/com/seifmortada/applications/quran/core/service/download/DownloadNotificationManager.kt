package com.seifmortada.applications.quran.core.service.download

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.seifmortada.applications.quran.core.domain.model.download.*
import com.seifmortada.applications.quran.core.service.utils.DownloadServiceConstants

/**
 * Manages notifications for the download service
 * Handles Android 13+ notification compatibility and proper channel management
 */
class DownloadNotificationManager(private val context: Context) {

    companion object {
        private const val TAG = "DownloadNotificationManager"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Verify the notification channel exists
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val existingChannel =
                notificationManager.getNotificationChannel(DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            if (existingChannel == null) {
                throw IllegalStateException("Notification channel not found. Ensure Application class creates the channel.")
            }
        }
    }

    /**
     * Creates notification for download starting
     */
    fun createStartingNotification(downloadRequest: DownloadRequest): Notification {
        val displayName = downloadRequest.surahNameEn ?: "Surah ${downloadRequest.surahNumber}"

        return NotificationCompat.Builder(context, DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Starting Download")
            .setContentText("${downloadRequest.reciterName} - $displayName")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setProgress(100, 0, true) // Indeterminate progress
            .addAction(createCancelAction(downloadRequest.downloadId))
            .build()
    }

    /**
     * Creates notification for download progress
     */
    fun createProgressNotification(
        downloadRequest: DownloadRequest,
        downloadProgress: DownloadProgress
    ): Notification {
        val displayName = downloadRequest.surahNameEn ?: "Surah ${downloadRequest.surahNumber}"
        val progressText =
            "${downloadProgress.getFormattedProgress()} - ${downloadProgress.getFormattedSize()}"

        return NotificationCompat.Builder(context, DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Downloading ${downloadRequest.reciterName}")
            .setContentText("$displayName - $progressText")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, downloadProgress.progressPercentage, false)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .addAction(createCancelAction(downloadRequest.downloadId))
            .apply {
                // Add download speed info if available
                if (downloadProgress.downloadSpeed > 0) {
                    val speedText = formatSpeed(downloadProgress.downloadSpeed)
                    setSubText(speedText)
                }
            }
            .build()
    }

    /**
     * Creates notification for download completion
     */
    fun createCompletedNotification(
        downloadRequest: DownloadRequest,
        filePath: String
    ): Notification {
        val displayName = downloadRequest.surahNameEn ?: "Surah ${downloadRequest.surahNumber}"

        return NotificationCompat.Builder(context, DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Download Completed")
            .setContentText("${downloadRequest.reciterName} - $displayName")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()
    }

    /**
     * Creates notification for download failure
     */
    fun createFailedNotification(downloadRequest: DownloadRequest, error: String): Notification {
        val displayName = downloadRequest.surahNameEn ?: "Surah ${downloadRequest.surahNumber}"

        return NotificationCompat.Builder(context, DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Download Failed")
            .setContentText("${downloadRequest.reciterName} - $displayName")
            .setStyle(NotificationCompat.BigTextStyle().bigText(error))
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .build()
    }

    /**
     * Creates notification for download cancellation
     */
    fun createCancelledNotification(downloadRequest: DownloadRequest): Notification {
        val displayName = downloadRequest.surahNameEn ?: "Surah ${downloadRequest.surahNumber}"

        return NotificationCompat.Builder(context, DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Download Cancelled")
            .setContentText("${downloadRequest.reciterName} - $displayName")
            .setSmallIcon(android.R.drawable.ic_menu_close_clear_cancel)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()
    }

    /**
     * Updates notification for the current download status
     */
    fun updateNotification(downloadRequest: DownloadRequest, status: DownloadStatus) {
        val notification = when (status) {
            is DownloadStatus.Starting -> createStartingNotification(downloadRequest)
            is DownloadStatus.InProgress -> createProgressNotification(
                downloadRequest,
                status.downloadProgress
            )

            is DownloadStatus.Completed -> createCompletedNotification(
                downloadRequest,
                status.filePath
            )

            is DownloadStatus.Failed -> createFailedNotification(downloadRequest, status.error)
            is DownloadStatus.Cancelled -> createCancelledNotification(downloadRequest)
            else -> null
        }

        notification?.let {
            notificationManager.notify(DownloadServiceConstants.NOTIFICATION_ID_DOWNLOAD, it)
        }
    }

    /**
     * Cancels the download notification
     */
    fun cancelNotification() {
        notificationManager.cancel(DownloadServiceConstants.NOTIFICATION_ID_DOWNLOAD)
    }

    /**
     * Creates cancel action for notification
     */
    private fun createCancelAction(downloadId: String): NotificationCompat.Action {
        val intent =
            createDownloadServiceIntent(DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD_BY_ID).apply {
                putExtra(DownloadServiceConstants.EXTRA_DOWNLOAD_ID, downloadId)
            }

        val pendingIntent = PendingIntent.getService(
            context,
            downloadId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Cancel",
            pendingIntent
        ).build()
    }

    /**
     * Creates intent for download service
     */
    private fun createDownloadServiceIntent(action: String): Intent {
        return Intent(action).apply {
            setPackage(context.packageName)
        }
    }

    /**
     * Formats download speed for display
     */
    private fun formatSpeed(bytesPerSecond: Long): String {
        return when {
            bytesPerSecond < 1024 -> "${bytesPerSecond}B/s"
            bytesPerSecond < 1024 * 1024 -> "${bytesPerSecond / 1024}KB/s"
            else -> "${bytesPerSecond / (1024 * 1024)}MB/s"
        }
    }

    /**
     * Checks if notification channel has proper importance for foreground services
     */
    fun hasProperChannelImportance(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                notificationManager.getNotificationChannel(DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)
            channel != null && channel.importance >= NotificationManager.IMPORTANCE_DEFAULT
        } else {
            true // Pre-O doesn't need channel importance
        }
    }
}