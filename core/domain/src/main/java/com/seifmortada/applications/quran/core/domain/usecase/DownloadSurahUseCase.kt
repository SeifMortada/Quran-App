package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.repository.DownloadRepository
import com.seifmortada.applications.quran.core.domain.repository.DownloadStatus
import kotlinx.coroutines.flow.Flow

class DownloadSurahUseCase(
    private val downloadRepository: DownloadRepository
) {

    operator fun invoke(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null,
        serverUrl: String
    ): Flow<DownloadStatus> {
        return downloadRepository.startSurahDownload(
            downloadUrl = downloadUrl,
            reciterName = reciterName,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn,
            serverUrl = serverUrl
        )
    }

    fun cancelDownload() {
        downloadRepository.cancelDownload()
    }

    fun isSurahDownloaded(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): Boolean {
        return downloadRepository.isSurahDownloaded(
            reciterName = reciterName,
            serverUrl = serverUrl,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn
        )
    }

    fun getSurahFilePath(
        reciterName: String,
        serverUrl: String,
        surahNumber: Int,
        surahNameAr: String? = null,
        surahNameEn: String? = null
    ): String? {
        return downloadRepository.getSurahFilePath(
            reciterName = reciterName,
            serverUrl = serverUrl,
            surahNumber = surahNumber,
            surahNameAr = surahNameAr,
            surahNameEn = surahNameEn
        )
    }
}