package com.seifmortada.applications.quran.core.domain.repository

import kotlinx.coroutines.flow.Flow
import java.io.Serializable

interface DownloadRepository {

    /**
     * Starts downloading a Surah
     * @return Flow of download status
     */
    fun startSurahDownload(
        downloadRequest: DownloadRequest
    ): Flow<DownloadStatus>

    /**
     * Cancels the current download
     */
    fun cancelDownload()

    /**
     * Cancels a specific download by ID
     */
    fun cancelDownload(downloadId: String)

    /**
     * Gets current download status
     */
    fun getCurrentDownloadStatus(): Flow<DownloadStatus>

    /**
     * Checks if a Surah is already downloaded
     */
    fun isSurahDownloaded(downloadRequest: DownloadRequest): Boolean

    /**
     * Gets the local file path for a downloaded Surah
     */
    fun getSurahFilePath(downloadRequest: DownloadRequest): String?

    /**
     * Gets all active downloads
     */
    fun getActiveDownloads(): Flow<List<DownloadInfo>>
}

/**
 * Domain entity representing a download request
 */
data class DownloadRequest(
    val downloadUrl: String,
    val reciterName: String,
    val surahNumber: Int,
    val surahNameAr: String? = null,
    val surahNameEn: String? = null,
    val serverUrl: String,
    val downloadId: String = generateDownloadId(reciterName, surahNumber, serverUrl)
) : Serializable {
    companion object {
        private fun generateDownloadId(
            reciterName: String,
            surahNumber: Int,
            serverUrl: String
        ): String {
            return "${reciterName}_${surahNumber}_${serverUrl.hashCode()}".replace(" ", "_")
        }
    }
}

/**
 * Domain entity representing download information
 */
data class DownloadInfo(
    val downloadId: String,
    val downloadRequest: DownloadRequest,
    val status: DownloadStatus,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

/**
 * Domain entity representing download progress details
 */
data class DownloadProgress(
    val progress: Float,
    val downloadedBytes: Long,
    val totalBytes: Long,
    val downloadSpeed: Long = 0L, // bytes per second
    val estimatedTimeRemaining: Long = 0L // milliseconds
) : Serializable {
    val progressPercentage: Int get() = (progress * 100).toInt()

    fun getFormattedProgress(): String = "${progressPercentage}%"

    fun getFormattedSize(): String {
        val downloadedMB = downloadedBytes / (1024 * 1024)
        val totalMB = totalBytes / (1024 * 1024)
        return "${downloadedMB}MB / ${totalMB}MB"
    }
}

/**
 * Sealed class representing different download states
 */
sealed class DownloadStatus : Serializable {
    object Idle : DownloadStatus()
    object Starting : DownloadStatus()
    data class InProgress(val downloadProgress: DownloadProgress) : DownloadStatus()
    data class Completed(val filePath: String, val completedAt: Long = System.currentTimeMillis()) :
        DownloadStatus()

    data class Failed(
        val error: String,
        val errorCode: DownloadErrorCode = DownloadErrorCode.UNKNOWN
    ) : DownloadStatus()
    object Cancelled : DownloadStatus()
    object Paused : DownloadStatus()
}

/**
 * Enum representing different types of download errors
 */
enum class DownloadErrorCode : Serializable {
    NETWORK_ERROR,
    STORAGE_ERROR,
    PERMISSION_ERROR,
    FILE_ALREADY_EXISTS,
    INVALID_URL,
    SERVER_ERROR,
    TIMEOUT,
    UNKNOWN
}