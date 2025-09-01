package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.repository.DownloadRepository
import com.seifmortada.applications.quran.core.domain.repository.DownloadRequest
import com.seifmortada.applications.quran.core.domain.repository.DownloadStatus
import com.seifmortada.applications.quran.core.domain.repository.DownloadInfo
import kotlinx.coroutines.flow.Flow

/**
 * Use case for starting a Surah download
 */
class StartSurahDownloadUseCase(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(downloadRequest: DownloadRequest): Flow<DownloadStatus> {
        return downloadRepository.startSurahDownload(downloadRequest)
    }
}

/**
 * Use case for canceling downloads
 */
class CancelDownloadUseCase(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke() {
        downloadRepository.cancelDownload()
    }

    operator fun invoke(downloadId: String) {
        downloadRepository.cancelDownload(downloadId)
    }
}

/**
 * Use case for checking if a Surah is downloaded
 */
class IsSurahDownloadedUseCase(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(downloadRequest: DownloadRequest): Boolean {
        return downloadRepository.isSurahDownloaded(downloadRequest)
    }
}

/**
 * Use case for getting Surah file path
 */
class GetSurahFilePathUseCase(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(downloadRequest: DownloadRequest): String? {
        return downloadRepository.getSurahFilePath(downloadRequest)
    }
}

/**
 * Use case for getting current download status
 */
class GetCurrentDownloadStatusUseCase(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(): Flow<DownloadStatus> {
        return downloadRepository.getCurrentDownloadStatus()
    }
}

/**
 * Use case for getting all active downloads
 */
class GetActiveDownloadsUseCase(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(): Flow<List<DownloadInfo>> {
        return downloadRepository.getActiveDownloads()
    }
}