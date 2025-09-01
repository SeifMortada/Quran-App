/*
package com.seifmortada.applications.quran.core.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import com.seifmortada.applications.quran.core.ui.permissions.PermissionManager
import java.io.File
import kotlin.jvm.java

object DownloadHelper {

    private const val TAG = "DownloadHelper"

    */
/**
     * Starts a Surah download with proper error handling and permission checks
     *//*

    fun startSurahDownload(
        context: Context,
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null,
        serverUrl: String,
        targetActivityClass: Class<*>
    ): Boolean {
        // Check permissions first
        if (!PermissionManager.hasNotificationPermission(context) ||
            !PermissionManager.hasStoragePermission(context)
        ) {
            return false
        }

        // Check if file already exists
        val fileManager = QuranFileManager(context)
        if (fileManager.surahFileExists(
                reciterName,
                serverUrl,
                surahNumber,
                surahNameAr,
                surahNameEn
            )
        ) {
            // File already exists, no need to download
            return true
        }

        val intent = Intent(context, DownloadService::class.java).apply {
            action = DownloadService.ACTION_START_DOWNLOAD
            putExtra(DownloadService.EXTRA_DOWNLOAD_URL, downloadUrl)
            putExtra(DownloadService.EXTRA_RECITER_NAME, reciterName)
            putExtra(DownloadService.EXTRA_SURAH_NUMBER, surahNumber)
            putExtra(DownloadService.EXTRA_SURAH_NAME_AR, surahNameAr)
            putExtra(DownloadService.EXTRA_SURAH_NAME_EN, surahNameEn)
            putExtra(DownloadService.EXTRA_SERVER_URL, serverUrl)
            putExtra(DownloadService.EXTRA_TARGET_ACTIVITY_CLASS, targetActivityClass.name)
        }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    */
/**
     * Starts a Surah download with proper error handling and permission checks
     *//*

    fun startSurahDownload(
        context: Context,
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null,
        serverUrl: String
    ): Boolean {
        // Get the launcher activity class dynamically
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val launcherActivity = intent?.component?.className

        if (launcherActivity != null) {
            try {
                val activityClass = Class.forName(launcherActivity)
                Log.d(TAG, "Using activity class from manifest: $launcherActivity")
                return startSurahDownload(
                    context,
                    downloadUrl,
                    reciterName,
                    surahNumber,
                    surahNameAr,
                    surahNameEn,
                    serverUrl,
                    activityClass
                )
            } catch (e: ClassNotFoundException) {
                Log.w(TAG, "Activity class from manifest not found: $launcherActivity", e)
                // If the class from manifest doesn't exist, try common MainActivity patterns
                val commonActivityNames = listOf(
                    "${context.packageName}.MainActivity",
                    "${context.packageName}.ui.MainActivity",
                    "${context.packageName}.main.MainActivity",
                    "com.seifmortada.applications.quran.MainActivity"
                )

                for (activityName in commonActivityNames) {
                    try {
                        val activityClass = Class.forName(activityName)
                        Log.d(TAG, "Using common activity class: $activityName")
                        return startSurahDownload(
                            context,
                            downloadUrl,
                            reciterName,
                            surahNumber,
                            surahNameAr,
                            surahNameEn,
                            serverUrl,
                            activityClass
                        )
                    } catch (e2: ClassNotFoundException) {
                        Log.w(TAG, "Common activity class not found: $activityName", e2)
                        // Continue to next pattern
                        continue
                    }
                }

                // If all patterns fail, fallback to basic implementation without specific activity
                Log.w(TAG, "No activity class found, using basic implementation")
                return startSurahDownloadWithoutActivityClass(
                    context,
                    downloadUrl,
                    reciterName,
                    surahNumber,
                    surahNameAr,
                    surahNameEn,
                    serverUrl
                )
            }
        }

        // Fallback if launcher activity cannot be determined
        Log.w(TAG, "Launcher activity cannot be determined, using basic implementation")
        return startSurahDownloadWithoutActivityClass(
            context,
            downloadUrl,
            reciterName,
            surahNumber,
            surahNameAr,
            surahNameEn,
            serverUrl
        )
    }

    */
/**
     * Private helper method for downloads without specific activity class
     *//*

    private fun startSurahDownloadWithoutActivityClass(
        context: Context,
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?,
        serverUrl: String
    ): Boolean {
        // Check permissions first
        if (!PermissionManager.hasNotificationPermission(context) ||
            !PermissionManager.hasStoragePermission(context)
        ) {
            return false
        }

        // Check if file already exists
        val fileManager = QuranFileManager(context)
        if (fileManager.surahFileExists(
                reciterName,
                serverUrl,
                surahNumber,
                surahNameAr,
                surahNameEn
            )
        ) {
            // File already exists, no need to download
            return true
        }

        val intent = Intent(context, DownloadService::class.java).apply {
            action = DownloadService.ACTION_START_DOWNLOAD
            putExtra(DownloadService.EXTRA_DOWNLOAD_URL, downloadUrl)
            putExtra(DownloadService.EXTRA_RECITER_NAME, reciterName)
            putExtra(DownloadService.EXTRA_SURAH_NUMBER, surahNumber)
            putExtra(DownloadService.EXTRA_SURAH_NAME_AR, surahNameAr)
            putExtra(DownloadService.EXTRA_SURAH_NAME_EN, surahNameEn)
            putExtra(DownloadService.EXTRA_SERVER_URL, serverUrl)
            // Use the launcher activity class name or a default empty string
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(context.packageName)
            val activityClassName = launchIntent?.component?.className ?: ""
            putExtra(DownloadService.EXTRA_TARGET_ACTIVITY_CLASS, activityClassName)
        }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    */
/**
     * Cancels the current download
     *//*

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

    */
/**
     * Gets the local file path for a Surah if it exists
     *//*

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

    */
/**
     * Checks if a Surah is already downloaded
     *//*

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

    */
/**
     * Deletes a downloaded Surah
     *//*

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

    */
/**
     * Gets storage information
     *//*

    fun getStorageInfo(context: Context): StorageInfo {
        val fileManager = QuranFileManager(context)
        val totalSize = fileManager.getTotalDownloadedSize()
        return StorageInfo(
            totalDownloadedSize = totalSize,
            formattedSize = fileManager.formatFileSize(totalSize),
            baseDirectory = fileManager.getQuranAudioDirectory().absolutePath
        )
    }

    */
/**
     * Gets all downloaded Surah files for a specific reciter
     *//*

    fun getDownloadedSurahs(
        context: Context,
        reciterName: String,
        serverUrl: String
    ): List<File> {
        val fileManager = QuranFileManager(context)
        return fileManager.getDownloadedSurahs(reciterName, serverUrl)
    }

    */
/**
     * Deletes all files for a specific reciter
     *//*

    fun deleteReciterFiles(
        context: Context,
        reciterName: String,
        serverUrl: String
    ): Boolean {
        val fileManager = QuranFileManager(context)
        return fileManager.deleteReciterFiles(reciterName, serverUrl)
    }

    */
/**
     * Gets the total size of downloaded files for a specific reciter
     *//*

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
*/
