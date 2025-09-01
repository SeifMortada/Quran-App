package com.seifmortada.applications.quran.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seifmortada.applications.quran.core.domain.repository.DownloadRepository
import com.seifmortada.applications.quran.core.domain.repository.DownloadStatus
import com.seifmortada.applications.quran.core.service.DownloadHelper
import com.seifmortada.applications.quran.service.DownloadService
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DownloadRepositoryImpl(
    private val context: Context,
    private val fileManager: QuranFileManager
) : DownloadRepository {

    companion object {
        private const val TAG = "DownloadRepositoryImpl"
    }

    override fun startSurahDownload(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?,
        serverUrl: String
    ): Flow<DownloadStatus> = callbackFlow {

        // Check if file is already downloaded using DownloadHelper
        if (DownloadHelper.isSurahDownloaded(
                context = context,
                reciterName = reciterName,
                serverUrl = serverUrl,
                surahNumber = surahNumber,
                surahNameAr = surahNameAr,
                surahNameEn = surahNameEn
            )
        ) {
            val filePath = DownloadHelper.getSurahFilePath(
                context = context,
                reciterName = reciterName,
                serverUrl = serverUrl,
                surahNumber = surahNumber,
                surahNameAr = surahNameAr,
                surahNameEn = surahNameEn
            )
            if (filePath != null) {
                trySend(DownloadStatus.Completed(filePath))
                close()
                return@callbackFlow
            }
        }

        val localBroadcastManager = LocalBroadcastManager.getInstance(context)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    DownloadService.BROADCAST_DOWNLOAD_PROGRESS -> {
                        val progress = intent.getFloatExtra(DownloadService.EXTRA_PROGRESS, 0f)
                        val downloadedBytes =
                            intent.getLongExtra(DownloadService.EXTRA_DOWNLOADED_BYTES, 0L)
                        val totalBytes = intent.getLongExtra(DownloadService.EXTRA_TOTAL_BYTES, 0L)

                        trySend(DownloadStatus.InProgress(progress, downloadedBytes, totalBytes))
                    }

                    DownloadService.BROADCAST_DOWNLOAD_COMPLETED -> {
                        val filePath = intent.getStringExtra(DownloadService.EXTRA_FILE_PATH) ?: ""
                        trySend(DownloadStatus.Completed(filePath))
                        close()
                    }

                    DownloadService.BROADCAST_DOWNLOAD_FAILED -> {
                        val errorMessage =
                            intent.getStringExtra(DownloadService.EXTRA_ERROR_MESSAGE)
                                ?: "Download failed"
                        trySend(DownloadStatus.Failed(errorMessage))
                        close()
                    }

                    DownloadService.BROADCAST_DOWNLOAD_CANCELLED -> {
                        trySend(DownloadStatus.Cancelled)
                        close()
                    }
                }
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction(DownloadService.BROADCAST_DOWNLOAD_PROGRESS)
            addAction(DownloadService.BROADCAST_DOWNLOAD_COMPLETED)
            addAction(DownloadService.BROADCAST_DOWNLOAD_FAILED)
            addAction(DownloadService.BROADCAST_DOWNLOAD_CANCELLED)
        }

        localBroadcastManager.registerReceiver(receiver, intentFilter)

        // Start the download using the proven DownloadHelper
        try {
            trySend(DownloadStatus.Starting)

            val success = DownloadHelper.startSurahDownload(
                context = context,
                downloadUrl = downloadUrl,
                reciterName = reciterName,
                surahNumber = surahNumber,
                surahNameAr = surahNameAr,
                surahNameEn = surahNameEn,
                serverUrl = serverUrl
            )

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

    override fun cancelDownload() {
        DownloadHelper.cancelCurrentDownload(context)
    }

    override fun isSurahDownloaded(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?
    ): Boolean {
        return DownloadHelper.isSurahDownloaded(
            context = context,
            reciterName = reciterName,
            serverUrl = serverUrl,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn
        )
    }

    override fun getSurahFilePath(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?
    ): String? {
        return DownloadHelper.getSurahFilePath(
            context = context,
            reciterName = reciterName,
            serverUrl = serverUrl,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn
        )
    }
}