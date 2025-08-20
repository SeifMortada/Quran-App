package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import com.example.domain.model.SurahModel

data class ReciterSurahRecitationUiState(
    val fileSize: Long = 0L,
    val title: String = "",
    val currentSurah: SurahModel? = null,
    val audioPlayerState: AudioPlayerState = AudioPlayerState(),
    val searchQuery: String = ""
)

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val duration: Int = 0,
    val isPrepared: Boolean = false,
    val audioUrl: String = ""
)
