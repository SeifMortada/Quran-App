package com.seifmortada.applications.quran.core.service.download

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.seifmortada.applications.quran.core.domain.repository.DownloadRequest
import com.seifmortada.applications.quran.core.service.utils.DownloadServiceConstants
import com.seifmortada.applications.quran.core.ui.QuranFileManager

/**
 * Helper class that provides clean architecture access to the download service
 * Acts as a bridge between the domain layer and the service implementation
 */
object DownloadServiceHelper {

    private const val TAG = "DownloadServiceHelper"

    /**
     * Starts a Surah download using the clean architecture DownloadRequest
     */
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

    /**
     * Starts a Surah download with individual parameters (backward compatibility)
     */
    fun startSurahDownload(
        context: Context,
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null,
        serverUrl: String
    ): Boolean {
        val downloadRequest = DownloadRequest(
            downloadUrl = downloadUrl,
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )

        return startSurahDownload(context, downloadRequest)
    }

    /**
     * Cancels the current download - only if there's an active download
     */
    fun cancelCurrentDownload(context: Context) {
        // Don't start service just to cancel - this could cause crashes
        // The service will handle cancellation if it's already running
        try {
            val intent = Intent(context, QuranDownloadService::class.java).apply {
                action = DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD
            }
            // Use regular startService instead of startForegroundService for cancellation
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send cancel request", e)
        }
    }

    /**
     * Cancels a specific download by ID - only if there's an active download
     */
    fun cancelDownload(context: Context, downloadId: String) {
        try {
            val intent = Intent(context, QuranDownloadService::class.java).apply {
                action = DownloadServiceConstants.ACTION_CANCEL_DOWNLOAD_BY_ID
                putExtra(DownloadServiceConstants.EXTRA_DOWNLOAD_ID, downloadId)
            }
            // Use regular startService instead of startForegroundService for cancellation
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cancel download by ID: $downloadId", e)
        }
    }

    /**
     * Checks if a Surah is already downloaded using DownloadRequest
     */
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

    /**
     * Checks if a Surah is already downloaded using individual parameters
     */
    fun isSurahDownloaded(
        context: Context,
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val downloadRequest = DownloadRequest(
            downloadUrl = "", // Not needed for file check
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return isSurahDownloaded(context, downloadRequest)
    }

    /**
     * Gets the local file path for a downloaded Surah using DownloadRequest
     */
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

    /**
     * Gets the local file path for a downloaded Surah using individual parameters
     */
    fun getSurahFilePath(
        context: Context,
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): String? {
        val downloadRequest = DownloadRequest(
            downloadUrl = "", // Not needed for file path
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return getSurahFilePath(context, downloadRequest)
    }

    /**
     * Safely starts a service considering Android version compatibility
     */
    private fun startServiceSafely(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    /**
     * Storage information data class
     */
    data class StorageInfo(
        val totalDownloadedSize: Long,
        val formattedSize: String,
        val baseDirectory: String
    )

    /**
     * Gets storage information for downloads
     */
    fun getStorageInfo(context: Context): StorageInfo {
        val fileManager = QuranFileManager(context)
        val totalSize = fileManager.getTotalDownloadedSize()
        return StorageInfo(
            totalDownloadedSize = totalSize,
            formattedSize = fileManager.formatFileSize(totalSize),
            baseDirectory = fileManager.getQuranAudioDirectory().absolutePath
        )
    }

    /**
     * Deletes a downloaded Surah using DownloadRequest
     */
    fun deleteSurah(context: Context, downloadRequest: DownloadRequest): Boolean {
        val fileManager = QuranFileManager(context)
        return fileManager.deleteSurahFile(
            downloadRequest.reciterName,
            downloadRequest.serverUrl,
            downloadRequest.surahNumber,
            downloadRequest.surahNameAr,
            downloadRequest.surahNameEn
        )
    }

    /**
     * Deletes a downloaded Surah using individual parameters
     */
    fun deleteSurah(
        context: Context,
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val downloadRequest = DownloadRequest(
            downloadUrl = "", // Not needed for deletion
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return deleteSurah(context, downloadRequest)
    }
}