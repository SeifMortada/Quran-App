package com.seifmortada.applications.quran.features.reciter.recitation

import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.service.Audio

data class ReciterSurahRecitationUiState(
    val fileSize: Long = 0L,
    val title: String = "",
    val currentSurah: SurahModel? = null,
    val audioPlayerState: AudioPlayerState = AudioPlayerState()
)

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val isPrepared: Boolean = false,
    val audio: Audio? = null,
)
