package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

sealed interface FileDownloadEvent {
    object Idle : FileDownloadEvent
    data class InProgress(val progress: Float) : FileDownloadEvent
    data class Finished(val filePath: String) : FileDownloadEvent
    data class Error(val message: String) : FileDownloadEvent
    object Cancelled : FileDownloadEvent
}