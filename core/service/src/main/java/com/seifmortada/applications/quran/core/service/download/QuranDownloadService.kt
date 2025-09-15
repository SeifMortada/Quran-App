@file:Suppress("DEPRECATION")

package com.seifmortada.applications.quran.core.service.download

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seifmortada.applications.quran.core.domain.repository.*
import com.seifmortada.applications.quran.core.service.utils.DownloadServiceConstants
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Clean architecture-based Download Service
 * Handles Quran audio downloads with proper separation of concerns
 */
class QuranDownloadService : Service() {

    companion object {
        private const val TAG = "QuranDownloadService"
    }

    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: DownloadNotificationManager
    private lateinit var localBroadcastManager: LocalBroadcastManager

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var currentDownloadRequest: DownloadRequest? = null
    private var currentDownloadJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize dependencies
        val fileManager = QuranFileManager(this)
        downloadManager = DownloadManager(this, fileManager)
        notificationManager = DownloadNotificationManager(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            DownloadServiceConstants.ACTION_START_DOWNLOAD -> {
                handleStartDownload(intent)
            }

            DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD -> {
                handleCancelDownload()
            }

            DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD_BY_ID -> {
                val downloadId = intent.getStringExtra(DownloadServiceConstants.EXTRA_DOWNLOAD_ID)
                if (downloadId != null) {
                    handleCancelDownloadById(downloadId)
                }
            }

            else -> {
                Timber.tag(TAG).w("Unknown action: ${intent?.action}")
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        currentDownloadJob?.cancel()
        serviceScope.cancel()
        notificationManager.cancelNotification()
    }

    /**
     * Handles starting a new download
     */
    private fun handleStartDownload(intent: Intent) {
        try {
            val downloadRequest = extractDownloadRequest(intent)
            if (downloadRequest == null) {
                stopSelf()
                return
            }

            currentDownloadRequest = downloadRequest

            // Check notification channel before starting foreground
            if (!notificationManager.hasProperChannelImportance()) {
                Log.w(TAG, "Notification channel importance may cause issues on Android 13+")
            }

            // Additional safety check - verify channel exists
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val systemNotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel =
                    systemNotificationManager.getNotificationChannel(DownloadServiceConstants.NOTIFICATION_CHANNEL_ID)

                if (channel == null) {
                    Timber.tag(TAG).e("Notification channel does not exist - stopping service")
                    stopSelf()
                    return
                }

                if (channel.importance < NotificationManager.IMPORTANCE_DEFAULT) {
                    Timber.tag(TAG)
                        .w("Channel importance below required level for Android 13+")
                }

            }

            // Start as foreground service with initial notification
            val initialNotification =
                notificationManager.createStartingNotification(downloadRequest)

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(
                        DownloadServiceConstants.NOTIFICATION_ID_DOWNLOAD,
                        initialNotification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                    )
                } else {
                    startForeground(
                        DownloadServiceConstants.NOTIFICATION_ID_DOWNLOAD,
                        initialNotification
                    )
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to start foreground service")
                stopSelf()
                return
            }

            // Start the download
            currentDownloadJob = downloadManager
                .startDownload(downloadRequest)
                .onEach { status ->
                    handleDownloadStatusUpdate(downloadRequest, status)
                }
                .launchIn(serviceScope)

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Error handling start download")
            stopSelf()
        }
    }

    /**
     * Handles canceling the current download
     */
    private fun handleCancelDownload() {
        // If there's no active download, just stop the service without starting foreground
        if (currentDownloadRequest == null || currentDownloadJob?.isActive != true) {
            stopSelf()
            return
        }

        currentDownloadJob?.cancel()
        downloadManager.cancelAllDownloads()

        currentDownloadRequest?.let { request ->
            broadcastDownloadStatus(request, DownloadStatus.Cancelled)
            notificationManager.updateNotification(request, DownloadStatus.Cancelled)
        }

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Handles canceling a specific download by ID
     */
    private fun handleCancelDownloadById(downloadId: String) {
        // If there's no active download or it doesn't match, just stop
        if (currentDownloadRequest?.downloadId != downloadId) {
            stopSelf()
            return
        }

        handleCancelDownload()
    }

    /**
     * Handles download status updates
     */
    private fun handleDownloadStatusUpdate(
        downloadRequest: DownloadRequest,
        status: DownloadStatus
    ) {
        // Update notification
        notificationManager.updateNotification(downloadRequest, status)

        // Broadcast status update
        broadcastDownloadStatus(downloadRequest, status)

        // Handle terminal states
        when (status) {
            is DownloadStatus.Completed -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

            is DownloadStatus.Failed -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

            is DownloadStatus.Cancelled -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

            else -> {
                // Continue with ongoing states (Starting, InProgress)
            }
        }
    }

    /**
     * Broadcasts download status update
     */
    private fun broadcastDownloadStatus(downloadRequest: DownloadRequest, status: DownloadStatus) {
        val intent = Intent(DownloadServiceConstants.BROADCAST_DOWNLOAD_STATUS_CHANGED).apply {
            // For backward compatibility, we'll use JSON or individual fields for complex objects
            putExtra("download_id", downloadRequest.downloadId)
            putExtra("reciter_name", downloadRequest.reciterName)
            putExtra("surah_number", downloadRequest.surahNumber)
            putExtra("surah_name_ar", downloadRequest.surahNameAr)
            putExtra("surah_name_en", downloadRequest.surahNameEn)
            putExtra("server_url", downloadRequest.serverUrl)

            // Status information
            when (status) {
                is DownloadStatus.Starting -> {
                    putExtra("status_type", "starting")
                }

                is DownloadStatus.InProgress -> {
                    putExtra("status_type", "progress")
                    putExtra("progress", status.downloadProgress.progress)
                    putExtra("downloaded_bytes", status.downloadProgress.downloadedBytes)
                    putExtra("total_bytes", status.downloadProgress.totalBytes)
                    putExtra("download_speed", status.downloadProgress.downloadSpeed)
                }

                is DownloadStatus.Completed -> {
                    putExtra("status_type", "completed")
                    putExtra("file_path", status.filePath)
                }

                is DownloadStatus.Failed -> {
                    putExtra("status_type", "failed")
                    putExtra("error_message", status.error)
                    putExtra("error_code", status.errorCode.name)
                }

                is DownloadStatus.Cancelled -> {
                    putExtra("status_type", "cancelled")
                }

                else -> {
                    putExtra("status_type", "idle")
                }
            }
        }

        localBroadcastManager.sendBroadcast(intent)
    }

    /**
     * Extracts download request from intent
     */
    private fun extractDownloadRequest(intent: Intent): DownloadRequest? {
        return try {
            // First try to get the complete DownloadRequest object
            val downloadRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    DownloadServiceConstants.EXTRA_DOWNLOAD_REQUEST,
                    DownloadRequest::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra(DownloadServiceConstants.EXTRA_DOWNLOAD_REQUEST) as? DownloadRequest
            }

            downloadRequest ?: extractLegacyDownloadRequest(intent)
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to extract DownloadRequest, trying legacy approach")
            // Fallback to legacy individual parameters for backward compatibility
            extractLegacyDownloadRequest(intent)
        }
    }

    /**
     * Extracts download request from legacy intent parameters
     * Maintains backward compatibility with old service calls
     */
    private fun extractLegacyDownloadRequest(intent: Intent): DownloadRequest? {
        return try {
            val downloadUrl = intent.getStringExtra("download_url")
            val reciterName = intent.getStringExtra("reciter_name")
            val surahNumber = intent.getIntExtra("surah_number", 0)
            val surahNameAr = intent.getStringExtra("surah_name_ar")
            val surahNameEn = intent.getStringExtra("surah_name_en")
            val serverUrl = intent.getStringExtra("server_url")

            if (downloadUrl != null && reciterName != null && serverUrl != null && surahNumber > 0) {
                DownloadRequest(
                    downloadUrl = downloadUrl,
                    reciterName = reciterName,
                    surahNumber = surahNumber,
                    surahNameAr = surahNameAr,
                    surahNameEn = surahNameEn,
                    serverUrl = serverUrl
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to extract legacy download request")
            null
        }
    }
}