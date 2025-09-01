package com.seifmortada.applications.quran.features.reciter.recitation

sealed interface AudioPlayerAction {
    data class LoadAudioPlayer(val url: String) : AudioPlayerAction
    data class SeekTo(val ms: Int) : AudioPlayerAction
    data object PlayPause : AudioPlayerAction
    data object FastForward : AudioPlayerAction
    data object FastRewind : AudioPlayerAction
}
