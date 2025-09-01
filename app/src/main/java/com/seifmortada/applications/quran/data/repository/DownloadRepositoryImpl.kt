package com.seifmortada.applications.quran.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seifmortada.applications.quran.core.domain.repository.*
import com.seifmortada.applications.quran.core.service.DownloadServiceConstants
import com.seifmortada.applications.quran.core.service.download.DownloadServiceHelper
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Updated DownloadRepository implementation using the new clean architecture service
 * Maintains backward compatibility while providing clean architecture benefits
 */
class DownloadRepositoryImpl(
    private val context: Context,
    private val fileManager: QuranFileManager
) : DownloadRepository {

    companion object {
        private const val TAG = "DownloadRepositoryImpl"
    }

    private val localBroadcastManager = LocalBroadcastManager.getInstance(context)

    override fun startSurahDownload(downloadRequest: DownloadRequest): Flow<DownloadStatus> =
        callbackFlow {

        // Check if file is already downloaded using new helper
        if (DownloadServiceHelper.isSurahDownloaded(context, downloadRequest)) {
            val filePath = DownloadServiceHelper.getSurahFilePath(context, downloadRequest)
            if (filePath != null) {
                trySend(DownloadStatus.Completed(filePath))
                close()
                return@callbackFlow
            }
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                handleBroadcastReceived(intent) { status ->
                    trySend(status)
                    // Close flow for terminal states
                    when (status) {
                        is DownloadStatus.Completed,
                        is DownloadStatus.Failed,
                        is DownloadStatus.Cancelled -> close()
                        else -> {} // Continue for ongoing states
                    }
                }
            }
        }

        val intentFilter = IntentFilter(DownloadServiceConstants.BROADCAST_DOWNLOAD_STATUS_CHANGED)
        localBroadcastManager.registerReceiver(receiver, intentFilter)

        // Start the download using new helper
        try {
            trySend(DownloadStatus.Starting)

            val success = DownloadServiceHelper.startSurahDownload(context, downloadRequest)
            if (!success) {
                trySend(DownloadStatus.Failed("Could not start download. Check permissions."))
                close()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Failed to start download", e)
            trySend(DownloadStatus.Failed("Failed to start download: ${e.message}"))
            close()
        }

        awaitClose {
            localBroadcastManager.unregisterReceiver(receiver)
        }
    }

    // Backward compatibility method - convert old parameters to new DownloadRequest
    fun startSurahDownloadLegacy(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?,
        serverUrl: String
    ): Flow<DownloadStatus> {
        val downloadRequest = DownloadRequest(
            downloadUrl = downloadUrl,
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return startSurahDownload(downloadRequest)
    }

    override fun cancelDownload() {
        DownloadServiceHelper.cancelCurrentDownload(context)
    }

    override fun cancelDownload(downloadId: String) {
        DownloadServiceHelper.cancelDownload(context, downloadId)
    }

    override fun getCurrentDownloadStatus(): Flow<DownloadStatus> {
        // For now, return idle. This could be enhanced with persistent status tracking
        return flowOf(DownloadStatus.Idle)
    }

    override fun isSurahDownloaded(downloadRequest: DownloadRequest): Boolean {
        return DownloadServiceHelper.isSurahDownloaded(context, downloadRequest)
    }

    // Backward compatibility method
    fun isSurahDownloadedLegacy(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?
    ): Boolean {
        val downloadRequest = DownloadRequest(
            downloadUrl = "",
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return DownloadServiceHelper.isSurahDownloaded(context, downloadRequest)
    }

    override fun getSurahFilePath(downloadRequest: DownloadRequest): String? {
        return DownloadServiceHelper.getSurahFilePath(context, downloadRequest)
    }

    // Backward compatibility method
    fun getSurahFilePathLegacy(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?
    ): String? {
        val downloadRequest = DownloadRequest(
            downloadUrl = "",
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return DownloadServiceHelper.getSurahFilePath(context, downloadRequest)
    }

    override fun getActiveDownloads(): Flow<List<DownloadInfo>> {
        // For now, return empty. This could be enhanced with download tracking
        return flowOf(emptyList())
    }

    /**
     * Handles broadcast received from the download service
     * Updated to work with the new service's broadcast format
     */
    private fun handleBroadcastReceived(intent: Intent?, onStatus: (DownloadStatus) -> Unit) {
        if (intent == null) return

        val statusType = intent.getStringExtra("status_type") ?: return

        val status = when (statusType) {
            "starting" -> DownloadStatus.Starting

            "progress" -> {
                val progress = intent.getFloatExtra("progress", 0f)
                val downloadedBytes = intent.getLongExtra("downloaded_bytes", 0L)
                val totalBytes = intent.getLongExtra("total_bytes", 0L)
                val downloadSpeed = intent.getLongExtra("download_speed", 0L)

                val downloadProgress = DownloadProgress(
                    progress = progress,
                    downloadedBytes = downloadedBytes,
                    totalBytes = totalBytes,
                    downloadSpeed = downloadSpeed
                )
                DownloadStatus.InProgress(downloadProgress)
            }

            "completed" -> {
                val filePath = intent.getStringExtra("file_path") ?: ""
                DownloadStatus.Completed(filePath)
            }

            "failed" -> {
                val errorMessage = intent.getStringExtra("error_message") ?: "Download failed"
                val errorCodeName = intent.getStringExtra("error_code") ?: "UNKNOWN"
                val errorCode = try {
                    DownloadErrorCode.valueOf(errorCodeName)
                } catch (e: Exception) {
                    DownloadErrorCode.UNKNOWN
                }
                DownloadStatus.Failed(errorMessage, errorCode)
            }

            "cancelled" -> DownloadStatus.Cancelled

            else -> DownloadStatus.Idle
        }

        onStatus(status)
    }
}