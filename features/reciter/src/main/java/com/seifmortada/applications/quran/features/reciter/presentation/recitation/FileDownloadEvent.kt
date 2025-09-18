package com.seifmortada.applications.quran.features.reciter.presentation.recitation

sealed interface FileDownloadEvent {
    object Idle : FileDownloadEvent
    object Starting : FileDownloadEvent

    data class InProgress(
        val progress: Float,
        val downloadedBytes: Long = 0L,
        val totalBytes: Long = 0L,
        val downloadSpeed: Long = 0L, // bytes per second
        val estimatedTimeRemaining: Long = 0L // milliseconds
    ) : FileDownloadEvent {
        val progressPercentage: Int get() = (progress * 100).toInt()

        fun getFormattedProgress(): String = "${progressPercentage}%"

        fun getFormattedSize(): String {
            val downloadedMB = downloadedBytes / (1024 * 1024)
            val totalMB = totalBytes / (1024 * 1024)
            return "${downloadedMB}MB / ${totalMB}MB"
        }

        fun getFormattedSpeed(): String {
            return when {
                downloadSpeed < 1024 -> "${downloadSpeed}B/s"
                downloadSpeed < 1024 * 1024 -> "${downloadSpeed / 1024}KB/s"
                else -> "${downloadSpeed / (1024 * 1024)}MB/s"
            }
        }

        fun getFormattedTimeRemaining(): String {
            if (estimatedTimeRemaining <= 0) return ""
            val seconds = estimatedTimeRemaining / 1000
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return if (minutes > 0) {
                "${minutes}m ${remainingSeconds}s"
            } else {
                "${remainingSeconds}s"
            }
        }
    }

    data class Finished(val filePath: String) : FileDownloadEvent
    data class Error(val message: String) : FileDownloadEvent
    object Cancelled : FileDownloadEvent
}
