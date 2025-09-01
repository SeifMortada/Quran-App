package com.seifmortada.applications.quran.core.service.audio

data class Audio(
    val title: String,
    val path: String,
    val duration: Int,
    val reciterName: String = "",
    val surahInfo: String = ""
)
