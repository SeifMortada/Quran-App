package com.seifmortada.applications.quran.core.domain.repository

import com.seifmortada.applications.quran.core.domain.model.download.DownloadInfo
import com.seifmortada.applications.quran.core.domain.model.download.DownloadRequest
import com.seifmortada.applications.quran.core.domain.model.download.DownloadStatus
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun startSurahDownload(
        downloadRequest: DownloadRequest
    ): Flow<DownloadStatus>


    fun cancelDownload()


    fun cancelDownload(downloadId: String)


    fun getCurrentDownloadStatus(): Flow<DownloadStatus>


    fun isSurahDownloaded(downloadRequest: DownloadRequest): Boolean


    fun getSurahFilePath(downloadRequest: DownloadRequest): String?


    fun getActiveDownloads(): Flow<List<DownloadInfo>>
}
