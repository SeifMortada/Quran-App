package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.core.service.AudioPlayerService
import com.seifmortada.applications.quran.core.service.FORWARD
import com.seifmortada.applications.quran.core.service.PLAYPAUSE
import com.seifmortada.applications.quran.core.service.REWIND
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReciterSurahRecitationViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val getSurahRecitationUseCase: GetSurahRecitationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReciterSurahRecitationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<FileDownloadEvent>()
    val event = _event.receiveAsFlow()

    private var audioService: AudioPlayerService? = null
    private var serviceCollectJob: Job? = null

    fun fetchRecitation(server: String, surahNumber: Int) = viewModelScope.launch {
        val currentSurah = getSurahByIdUseCase(surahNumber)
        _uiState.update { it.copy(currentSurah = currentSurah) }

        getSurahRecitationUseCase(server, surahNumber.toString())
            .collect { progress ->
                val clamped = (progress.progress).coerceIn(0f, 1f)
                _uiState.update {
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
        _uiState.update {
            it.copy(
                searchQuery = com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering(
                    query
                )
            )
        }
    }

    fun bindService(service: AudioPlayerService) {
        audioService = service

        serviceCollectJob?.cancel()
        serviceCollectJob = viewModelScope.launch {
            combine(
                service.currentAudio,
                service.isPlaying,
                service.currentDuration
            ) { audio, isPlaying, current ->
                AudioPlayerState(
                    audio = audio,
                    isPlaying = isPlaying,
                    currentPosition = current
                )
            }.collect { audioPlayerState ->
                _uiState.update { it.copy(audioPlayerState = audioPlayerState) }
            }
        }
    }

    /**
     * Dispatch playback events to the AudioPlayerService
     */
    fun sendEvent(context: Context, audioEvent: AudioPlayerAction) {
        val intent = Intent(context, AudioPlayerService::class.java)

        when (audioEvent) {
            is AudioPlayerAction.LoadAudioPlayer -> {
                intent.action = com.seifmortada.applications.quran.core.service.AUDIO_LOAD
                intent.putExtra("AUDIO_PATH", audioEvent.url)
                startAudioService(context, intent)
            }

            is AudioPlayerAction.SeekTo -> {
                intent.action = com.seifmortada.applications.quran.core.service.SEEK_TO
                intent.putExtra("SEEK_POSITION", audioEvent.ms)
                startAudioService(context, intent)
            }

            AudioPlayerAction.PlayPause -> {
                intent.action = PLAYPAUSE
                startAudioService(context, intent)
            }

            AudioPlayerAction.FastForward -> {
                intent.action = FORWARD
                startAudioService(context, intent)
            }

            AudioPlayerAction.FastRewind -> {
                intent.action = REWIND
                startAudioService(context, intent)
            }
        }
    }

    private fun startAudioService(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanResources()
    }

    private fun cleanResources() {
        serviceCollectJob?.cancel()
        audioService?.stopPlayback()
        audioService = null
        _uiState.value = ReciterSurahRecitationUiState()
        _event.close()
    }


}
