package com.seifmortada.applications.quran.core.domain.model.download

import java.io.Serializable

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