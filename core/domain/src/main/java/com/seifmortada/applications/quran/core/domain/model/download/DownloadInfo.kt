package com.seifmortada.applications.quran.core.domain.model.download

import java.io.Serializable

data class DownloadInfo(
    val downloadId: String,
    val downloadRequest: DownloadRequest,
    val status: DownloadStatus,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable