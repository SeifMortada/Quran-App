package com.seifmortada.applications.quran.core.service.download

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.seifmortada.applications.quran.core.domain.model.download.DownloadRequest
import com.seifmortada.applications.quran.core.service.utils.DownloadServiceConstants
import com.seifmortada.applications.quran.core.ui.QuranFileManager

/**
 * Helper class that provides clean architecture access to the download service
 * Acts as a bridge between the domain layer and the service implementation
 */
object DownloadServiceHelper {

    private const val TAG = "DownloadServiceHelper"

    fun startSurahDownload(
        context: Context,
        downloadRequest: DownloadRequest
    ): Boolean {
        return try {
            val intent = Intent(context, QuranDownloadService::class.java).apply {
                action = DownloadServiceConstants.ACTION_START_DOWNLOAD
                putExtra(DownloadServiceConstants.EXTRA_DOWNLOAD_REQUEST, downloadRequest)
            }

            startServiceSafely(context, intent)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start download service", e)
            false
        }
    }

    fun cancelCurrentDownload(context: Context) {
        try {
            val intent = Intent(context, QuranDownloadService::class.java).apply {
                action = DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD
            }
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send cancel request", e)
        }
    }

    fun cancelDownload(context: Context, downloadId: String) {
        try {
            val intent = Intent(context, QuranDownloadService::class.java).apply {
                action = DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD_BY_ID
                putExtra(DownloadServiceConstants.EXTRA_DOWNLOAD_ID, downloadId)
            }
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cancel download by ID: $downloadId", e)
        }
    }

    fun isSurahDownloaded(context: Context, downloadRequest: DownloadRequest): Boolean {
        val fileManager = QuranFileManager(context)
        return fileManager.surahFileExists(
            downloadRequest.reciterName,
            downloadRequest.serverUrl,
            downloadRequest.surahNumber,
            downloadRequest.surahNameAr,
            downloadRequest.surahNameEn
        )
    }


    fun getSurahFilePath(context: Context, downloadRequest: DownloadRequest): String? {
        val fileManager = QuranFileManager(context)
        val file = fileManager.getSurahFilePath(
            downloadRequest.reciterName,
            downloadRequest.serverUrl,
            downloadRequest.surahNumber,
            downloadRequest.surahNameAr,
            downloadRequest.surahNameEn
        )
        return if (file.exists() && file.length() > 0) file.absolutePath else null
    }


    private fun startServiceSafely(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}