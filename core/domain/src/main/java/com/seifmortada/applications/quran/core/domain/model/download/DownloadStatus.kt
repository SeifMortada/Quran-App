package com.seifmortada.applications.quran.core.domain.model.download

import java.io.Serializable

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