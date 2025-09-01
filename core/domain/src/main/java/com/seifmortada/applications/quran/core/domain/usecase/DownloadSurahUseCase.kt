package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.repository.*
import kotlinx.coroutines.flow.Flow

/**
 * Use case for downloading Surah audio files
 * Updated to use the new clean architecture with DownloadRequest
 */
class DownloadSurahUseCase(
    private val downloadRepository: DownloadRepository
) {

    /**
     * Start downloading a Surah using the new DownloadRequest approach
     */
    operator fun invoke(downloadRequest: DownloadRequest): Flow<DownloadStatus> {
        return downloadRepository.startSurahDownload(downloadRequest)
    }

    /**
     * Start downloading a Surah using individual parameters (backward compatibility)
     */
    operator fun invoke(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null,
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
        return downloadRepository.startSurahDownload(downloadRequest)
    }

    /**
     * Cancel all downloads
     */
    fun cancelDownload() {
        downloadRepository.cancelDownload()
    }

    /**
     * Cancel a specific download by ID
     */
    fun cancelDownload(downloadId: String) {
        downloadRepository.cancelDownload(downloadId)
    }

    /**
     * Check if a Surah is downloaded using DownloadRequest
     */
    fun isSurahDownloaded(downloadRequest: DownloadRequest): Boolean {
        return downloadRepository.isSurahDownloaded(downloadRequest)
    }

    /**
     * Check if a Surah is downloaded using individual parameters (backward compatibility)
     */
    fun isSurahDownloaded(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        val downloadRequest = DownloadRequest(
            downloadUrl = "", // Not needed for checking
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
        return downloadRepository.isSurahDownloaded(downloadRequest)
    }

    /**
     * Get Surah file path using DownloadRequest
     */
    fun getSurahFilePath(downloadRequest: DownloadRequest): String? {
        return downloadRepository.getSurahFilePath(downloadRequest)
    }

    /**
     * Get Surah file path using individual parameters (backward compatibility)
     */
    fun getSurahFilePath(
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
        return downloadRepository.getSurahFilePath(downloadRequest)
    }

    /**
     * Get current download status
     */
    fun getCurrentDownloadStatus(): Flow<DownloadStatus> {
        return downloadRepository.getCurrentDownloadStatus()
    }

    /**
     * Get all active downloads
     */
    fun getActiveDownloads(): Flow<List<DownloadInfo>> {
        return downloadRepository.getActiveDownloads()
    }
}