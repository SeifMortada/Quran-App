package com.seifmortada.applications.quran.core.domain.model.download

import java.io.Serializable

data class DownloadRequest(
    val downloadUrl: String,
    val reciterName: String,
    val surahNumber: Int,
    val surahNameAr: String? = null,
    val surahNameEn: String? = null,
    val serverUrl: String,
    val downloadId: String = generateDownloadId(reciterName, surahNumber, serverUrl)
) : Serializable {
    companion object {
        private fun generateDownloadId(
            reciterName: String,
            surahNumber: Int,
            serverUrl: String
        ): String {
            return "${reciterName}_${surahNumber}_${serverUrl.hashCode()}".replace(" ", "_")
        }
    }
}