package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow

class ReciterSurahRecitationViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val getSurahRecitationUseCase: GetSurahRecitationUseCase
) : ViewModel() {

    private val _ReciterSurahRecitation_uiState = MutableStateFlow(ReciterSurahRecitationUiState())
    val uiState = _ReciterSurahRecitation_uiState.asStateFlow()

    private val _event = Channel<FileDownloadEvent>()
    val event = _event.receiveAsFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun fetchRecitation(server: String, surahNumber: Int) = viewModelScope.launch {

        val currentSurah = getSurahByIdUseCase(surahNumber)
        _ReciterSurahRecitation_uiState.update { it.copy(currentSurah = currentSurah) }

        getSurahRecitationUseCase(server, surahNumber.toString())
            .collect { progress ->
                val clamped = (progress.progress).coerceIn(0f, 1f)
                _ReciterSurahRecitation_uiState.update {
                    it.copy(
                        fileSize = progress.totalBytes,
                        title = "Downloading ${(clamped * 100).toInt()}%",
                    )
                }
                if (clamped < 1f) {
                    _event.send(FileDownloadEvent.InProgress(clamped))
                } else {
                    _event.send(FileDownloadEvent.Finished(progress.localPath.toString()))
                }

            }
    }

    fun searchQuery(query: String) {
        _ReciterSurahRecitation_uiState.update {
            it.copy(searchQuery = normalizeTextForFiltering(query))
        }
    }


    fun sendEvent(audioEvent: AudioPlayerAction) {
        when (audioEvent) {
            is AudioPlayerAction.LoadAudioPlayer -> loadAudio(url = audioEvent.url)
            is AudioPlayerAction.SeekTo -> seekTo(audioEvent.ms)
            AudioPlayerAction.PlayPause -> playPause()
            AudioPlayerAction.FastForward -> forward10s()
            AudioPlayerAction.FastRewind -> rewind10s()
        }
    }

    private fun loadAudio(url: String) {
        releasePlayer()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                _ReciterSurahRecitation_uiState.update { state ->
                    state.copy(
                        audioPlayerState = state.audioPlayerState.copy(
                            isPrepared = true,
                            duration = duration,
                            audioUrl = url
                        )
                    )
                }
            }
            setOnCompletionListener {
                _ReciterSurahRecitation_uiState.update { state ->
                    state.copy(
                        audioPlayerState = state.audioPlayerState.copy(
                            isPlaying = false,
                            currentPosition = 0
                        )
                    )
                }
            }
        }
    }

    private fun playPause() {
        val player = mediaPlayer ?: return
        val ps = _ReciterSurahRecitation_uiState.value.audioPlayerState

        if (ps.isPrepared) {
            if (player.isPlaying) {
                player.pause()
                _ReciterSurahRecitation_uiState.update { it.copy(audioPlayerState = ps.copy(isPlaying = false)) }
            } else {
                player.start()
                _ReciterSurahRecitation_uiState.update { it.copy(audioPlayerState = ps.copy(isPlaying = true)) }
                observeProgress()
            }
        }
    }

    private fun seekTo(ms: Int) {
        mediaPlayer?.seekTo(ms)
        _ReciterSurahRecitation_uiState.update { state ->
            state.copy(audioPlayerState = state.audioPlayerState.copy(currentPosition = ms))
        }
    }

    private fun rewind10s() {
        val newPos = (_ReciterSurahRecitation_uiState.value.audioPlayerState.currentPosition - 10_000).coerceAtLeast(0)
        seekTo(newPos)
    }

    private fun forward10s() {
        val ps = _ReciterSurahRecitation_uiState.value.audioPlayerState
        val newPos = (ps.currentPosition + 10_000).coerceAtMost(ps.duration)
        seekTo(newPos)
    }

    private fun observeProgress() {
        viewModelScope.launch {
            while (_ReciterSurahRecitation_uiState.value.audioPlayerState.isPlaying) {
                mediaPlayer?.let { player ->
                    _ReciterSurahRecitation_uiState.update { state ->
                        state.copy(
                            audioPlayerState = state.audioPlayerState.copy(
                                currentPosition = player.currentPosition
                            )
                        )
                    }
                }
                delay(1000)
            }
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
