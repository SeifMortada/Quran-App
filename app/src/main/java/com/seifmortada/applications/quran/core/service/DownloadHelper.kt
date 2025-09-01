package com.seifmortada.applications.quran.core.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import com.seifmortada.applications.quran.core.ui.permissions.PermissionManager
import com.seifmortada.applications.quran.service.DownloadService
import java.io.File
import android.util.Log

object DownloadHelper {

    private val TAG = "DownloadHelper"

    /**
     * Starts a Surah download with proper error handling and permission checks
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
        Log.d(TAG, "=== DownloadHelper.startSurahDownload START ===")
        Log.d(TAG, "Download params - URL: $downloadUrl")
        Log.d(TAG, "Reciter: $reciterName, Surah: $surahNumber")
        Log.d(TAG, "Server URL: $serverUrl")
        Log.d(TAG, "Surah Name AR: $surahNameAr, EN: $surahNameEn")

        // Check permissions first
        Log.d(TAG, "=== Checking permissions ===")
        val hasNotificationPermission = PermissionManager.hasNotificationPermission(context)
        val hasStoragePermission = PermissionManager.hasStoragePermission(context)

        Log.d(TAG, "Notification permission: $hasNotificationPermission")
        Log.d(TAG, "Storage permission: $hasStoragePermission")

        if (!hasNotificationPermission || !hasStoragePermission) {
            Log.e(TAG, "=== MISSING PERMISSIONS - Cannot start download ===")
            return false
        }

        // Check if file already exists
        Log.d(TAG, "=== Checking if file already exists ===")
        val fileManager = QuranFileManager(context)
        if (fileManager.surahFileExists(
                reciterName,
                serverUrl,
                surahNumber,
                surahNameAr,
                surahNameEn
            )
        ) {
            Log.d(TAG, "=== File already exists, no need to download ===")
            // File already exists, no need to download
            return true
        }

        Log.d(TAG, "=== Creating service intent ===")
        val intent = Intent(context, DownloadService::class.java).apply {
            action = DownloadService.ACTION_START_DOWNLOAD
            putExtra(DownloadService.EXTRA_DOWNLOAD_URL, downloadUrl)
            putExtra(DownloadService.EXTRA_RECITER_NAME, reciterName)
            putExtra(DownloadService.EXTRA_SURAH_NUMBER, surahNumber)
            putExtra(DownloadService.EXTRA_SURAH_NAME_AR, surahNameAr)
            putExtra(DownloadService.EXTRA_SURAH_NAME_EN, surahNameEn)
            putExtra(DownloadService.EXTRA_SERVER_URL, serverUrl)
        }

        Log.d(TAG, "=== Service intent created with all extras ===")
        Log.d(TAG, "Intent action: ${intent.action}")

        return try {
            Log.d(TAG, "=== About to start service ===")
            Log.d(TAG, "Android API Level: ${Build.VERSION.SDK_INT}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "=== Starting foreground service (API >= 26) ===")
                context.startForegroundService(intent)
            } else {
                Log.d(TAG, "=== Starting regular service (API < 26) ===")
                context.startService(intent)
            }

            Log.d(TAG, "=== Service started successfully ===")
            Log.d(TAG, "=== DownloadHelper.startSurahDownload COMPLETE ===")
            true
        } catch (e: Exception) {
            Log.e(TAG, "=== CRITICAL: Exception starting service ===", e)
            Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
            Log.e(TAG, "Exception message: ${e.message}")
            false
        }
    }

    /**
     * Cancels the current download
     */
    fun cancelCurrentDownload(context: Context) {
        val intent = Intent(context, DownloadService::class.java).apply {
            action = DownloadService.ACTION_CANCEL_DOWNLOAD
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (e: Exception) {
            // Ignore errors when canceling
        }
    }

    /**
     * Gets the local file path for a Surah if it exists
     */
    fun getSurahFilePath(
        context: Context,
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): String? {
        val fileManager = QuranFileManager(context)
        val file = fileManager.getSurahFilePath(
            reciterName,
            serverUrl,
            surahNumber,
            surahNameAr,
            surahNameEn
        )
        return if (file.exists() && file.length() > 0) {
            file.absolutePath
        } else {
            null
        }
    }

    /**
     * Checks if a Surah is already downloaded
     */
    fun isSurahDownloaded(
        context: Context,
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val fileManager = QuranFileManager(context)
        return fileManager.surahFileExists(
            reciterName,
            serverUrl,
            surahNumber,
            surahNameAr,
            surahNameEn
        )
    }

    /**
     * Deletes a downloaded Surah
     */
    fun deleteSurah(
        context: Context,
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val fileManager = QuranFileManager(context)
        return fileManager.deleteSurahFile(
            reciterName,
            serverUrl,
            surahNumber,
            surahNameAr,
            surahNameEn
        )
    }

    /**
     * Gets storage information
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
     * Gets all downloaded Surah files for a specific reciter
     */
    fun getDownloadedSurahs(
        context: Context,
        reciterName: String,
        serverUrl: String
    ): List<File> {
        val fileManager = QuranFileManager(context)
        return fileManager.getDownloadedSurahs(reciterName, serverUrl)
    }

    /**
     * Deletes all files for a specific reciter
     */
    fun deleteReciterFiles(
        context: Context,
        reciterName: String,
        serverUrl: String
    ): Boolean {
        val fileManager = QuranFileManager(context)
        return fileManager.deleteReciterFiles(reciterName, serverUrl)
    }

    /**
     * Gets the total size of downloaded files for a specific reciter
     */
    fun getReciterDownloadedSize(
        context: Context,
        reciterName: String,
        serverUrl: String
    ): Long {
        val fileManager = QuranFileManager(context)
        return fileManager.getReciterDownloadedSize(reciterName, serverUrl)
    }

    data class StorageInfo(
        val totalDownloadedSize: Long,
        val formattedSize: String,
        val baseDirectory: String
    )
}