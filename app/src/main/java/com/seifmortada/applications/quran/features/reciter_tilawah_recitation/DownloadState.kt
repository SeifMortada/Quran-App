package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

sealed class DownloadState {
    object Idle : DownloadState()
    data class InProgress(val progress: Float) : DownloadState()
    data class Finished(val filePath: String) : DownloadState()
    data class Error(val message: String) : DownloadState()
}