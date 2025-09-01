package com.seifmortada.applications.quran.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface DownloadRepository {

    /**
     * Starts downloading a Surah
     * @return Flow of download status
     */
    fun startSurahDownload(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null,
        serverUrl: String
    ): Flow<DownloadStatus>

    /**
     * Cancels the current download
     */
    fun cancelDownload()

    /**
     * Checks if a Surah is already downloaded
     */
    fun isSurahDownloaded(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean

    /**
     * Gets the local file path for a downloaded Surah
     */
    fun getSurahFilePath(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): String?
}

sealed class DownloadStatus {
    object Starting : DownloadStatus()
    data class InProgress(val progress: Float, val downloadedBytes: Long, val totalBytes: Long) :
        DownloadStatus()

    data class Completed(val filePath: String) : DownloadStatus()
    data class Failed(val error: String) : DownloadStatus()
    object Cancelled : DownloadStatus()
}