package com.seifmortada.applications.quran.core.ui

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.security.MessageDigest

class QuranFileManager(private val context: Context) {

    companion object {
        private const val TAG = "QuranFileManager"
        private const val QURAN_FOLDER = "QuranAudio"
        private const val SURAHS_FOLDER = "Surahs"
        private const val RECITERS_FOLDER = "Reciters"
    }

    /**
     * Gets the protected directory for storing Quran audio files
     * This directory is only accessible by this app and is automatically cleaned when app is uninstalled
     */
    fun getQuranAudioDirectory(): File {
        val baseDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Use app-specific directory (no permissions needed, automatically managed)
            File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), QURAN_FOLDER)
        } else {
            // Fallback to internal storage for very old devices
            File(context.filesDir, QURAN_FOLDER)
        }

        if (!baseDir.exists()) {
            val created = baseDir.mkdirs()
            Log.d(TAG, "Created Quran audio directory: $created - ${baseDir.absolutePath}")
        }

        return baseDir
    }

    /**
     * Gets the directory for storing Surah files organized by reciter
     */
    fun getSurahsDirectory(): File {
        val surahsDir = File(getQuranAudioDirectory(), SURAHS_FOLDER)
        if (!surahsDir.exists()) {
            surahsDir.mkdirs()
        }
        return surahsDir
    }

    /**
     * Gets a specific reciter's directory based on server URL for uniqueness
     */
    fun getReciterDirectory(reciterName: String, serverUrl: String): File {
        val reciterIdentifier = extractReciterIdentifierFromServer(serverUrl)
        val sanitizedReciterName = sanitizeFileName(reciterName)

        val directoryName = if (reciterIdentifier.isNotEmpty()) {
            reciterIdentifier
        } else {
            "${sanitizedReciterName}_${generateUniqueId(reciterName, serverUrl)}"
        }

        val reciterDir = File(getSurahsDirectory(), directoryName)
        if (!reciterDir.exists()) {
            reciterDir.mkdirs()
        }
        return reciterDir
    }

    /**
     * Gets a specific reciter's directory (legacy method for backward compatibility)
     */
    fun getReciterDirectory(reciterName: String): File {
        val sanitizedName = sanitizeFileName(reciterName)
        val reciterDir = File(getSurahsDirectory(), sanitizedName)
        if (!reciterDir.exists()) {
            reciterDir.mkdirs()
        }
        return reciterDir
    }

    /**
     * Generates a unique file name for a Surah based on reciter, server, and Surah number
     */
    fun generateSurahFileName(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): String {
        // Extract reciter identifier from server URL
        val reciterIdentifier = extractReciterIdentifierFromServer(serverUrl)

        // Format: 001_Al-Fatihah_الفاتحة_[reciterIdentifier].mp3
        val paddedNumber = surahNumber.toString().padStart(3, '0')
        val namesPart = buildString {
            surahNameEn?.let { append("_${sanitizeFileName(it)}") }
            surahNameAr?.let { append("_${sanitizeFileName(it)}") }
        }

        return "${paddedNumber}${namesPart}_${reciterIdentifier}.mp3"
    }

    /**
     * Extracts a unique reciter identifier from the server URL
     * Example: https://server8.mp3quran.net/ahmad_huth/ -> ahmad_huth_server8
     */
    private fun extractReciterIdentifierFromServer(serverUrl: String): String {
        return try {
            val url = serverUrl.removeSuffix("/")
            val pathSegments = url.split("/")

            // Get the reciter name (last path segment)
            val reciterPath = pathSegments.lastOrNull() ?: "unknown"

            // Get the server identifier (domain part)
            val domain = pathSegments.find { it.contains("server") } ?: "server"
            val serverIdentifier = domain.split(".").firstOrNull() ?: "server"

            // Combine: ahmad_huth_server8
            "${sanitizeFileName(reciterPath)}_${sanitizeFileName(serverIdentifier)}"
        } catch (e: Exception) {
            // Fallback to MD5 hash if parsing fails
            generateUniqueId(serverUrl, serverUrl)
        }
    }

    /**
     * Gets the full path for a Surah file
     */
    fun getSurahFilePath(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): File {
        val reciterDir = getReciterDirectory(reciterName, serverUrl)
        val fileName =
            generateSurahFileName(reciterName, serverUrl, surahNumber, surahNameAr, surahNameEn)
        return File(reciterDir, fileName)
    }

    /**
     * Checks if a Surah file already exists
     */
    fun surahFileExists(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val file = getSurahFilePath(reciterName, serverUrl, surahNumber, surahNameAr, surahNameEn)
        return file.exists() && file.length() > 0
    }

    /**
     * Gets all downloaded Surah files for a specific reciter
     */
    fun getDownloadedSurahs(reciterName: String, serverUrl: String): List<File> {
        val reciterDir = getReciterDirectory(reciterName, serverUrl)
        return reciterDir.listFiles { file ->
            file.isFile && file.extension.equals("mp3", ignoreCase = true)
        }?.toList() ?: emptyList()
    }

    /**
     * Deletes a specific Surah file
     */
    fun deleteSurahFile(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val file = getSurahFilePath(reciterName, serverUrl, surahNumber, surahNameAr, surahNameEn)
        return if (file.exists()) {
            val deleted = file.delete()
            Log.d(TAG, "Deleted Surah file: $deleted - ${file.absolutePath}")
            deleted
        } else {
            Log.w(TAG, "Surah file not found for deletion: ${file.absolutePath}")
            false
        }
    }

    /**
     * Deletes all files for a specific reciter
     */
    fun deleteReciterFiles(reciterName: String, serverUrl: String): Boolean {
        val reciterDir = getReciterDirectory(reciterName, serverUrl)
        return if (reciterDir.exists()) {
            val deleted = reciterDir.deleteRecursively()
            Log.d(TAG, "Deleted reciter directory: $deleted - ${reciterDir.absolutePath}")
            deleted
        } else {
            true // Already doesn't exist
        }
    }

    /**
     * Gets the total size of downloaded files in bytes
     */
    fun getTotalDownloadedSize(): Long {
        return getSurahsDirectory().walkTopDown()
            .filter { it.isFile }
            .sumOf { it.length() }
    }

    /**
     * Gets the total size of downloaded files for a specific reciter
     */
    fun getReciterDownloadedSize(reciterName: String, serverUrl: String): Long {
        val reciterDir = getReciterDirectory(reciterName, serverUrl)
        return reciterDir.walkTopDown()
            .filter { it.isFile }
            .sumOf { it.length() }
    }

    /**
     * Formats file size for display
     */
    fun formatFileSize(bytes: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB")
        var size = bytes.toDouble()
        var unitIndex = 0

        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024.0
            unitIndex++
        }

        return "%.2f %s".format(size, units[unitIndex])
    }

    private fun generateUniqueId(reciterName: String, serverUrl: String): String {
        val input = "${reciterName}_${serverUrl}"
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }.take(8)
    }

    private fun sanitizeFileName(name: String): String {
        // Remove or replace invalid characters for file names
        return name.replace(Regex("[<>:\"/\\\\|?*]"), "_")
            .replace(Regex("\\s+"), "_")
            .trim('_')
            .take(50) // Limit length
    }
}
