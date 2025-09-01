package com.seifmortada.applications.quran.features.reciter.recitation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.core.domain.repository.DownloadStatus
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahByIdUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.core.domain.usecase.DownloadSurahUseCase
import com.seifmortada.applications.quran.core.service.AudioPlayerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReciterSurahRecitationViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val getSurahRecitationUseCase: GetSurahRecitationUseCase,
    private val downloadSurahUseCase: DownloadSurahUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReciterSurahRecitationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<FileDownloadEvent>()
    val event = _event.receiveAsFlow()

    private var audioService: AudioPlayerService? = null
    private var serviceCollectJob: Job? = null
    private var downloadJob: Job? = null

    // Store download parameters for retry functionality
    private var lastDownloadParams: DownloadParams? = null

    data class DownloadParams(
        val context: Context,
        val server: String,
        val surahNumber: Int,
        val reciterName: String,
        val surahNameEn: String?,
        val surahNameAr: String?
    )

    /**
     * Enhanced method to fetch recitation with proper download management
     */
    fun fetchRecitation(
        context: Context,
        server: String,
        surahNumber: Int,
        reciterName: String = "Unknown Reciter",
        surahNameEn: String? = null,
        surahNameAr: String? = null
    ) = viewModelScope.launch {
        try {
            val currentSurah = getSurahByIdUseCase(surahNumber)
            _uiState.update { it.copy(currentSurah = currentSurah) }

            val effectiveSurahNameEn = surahNameEn ?: currentSurah?.name
            val effectiveSurahNameAr = surahNameAr ?: currentSurah?.name

            // Check if file is already downloaded
            val isDownloaded = downloadSurahUseCase.isSurahDownloaded(
                reciterName = reciterName,
                serverUrl = server,
                surahNumber = surahNumber,
                surahNameAr = effectiveSurahNameAr,
                surahNameEn = effectiveSurahNameEn
            )

            if (isDownloaded) {
                val filePath = downloadSurahUseCase.getSurahFilePath(
                    reciterName = reciterName,
                    serverUrl = server,
                    surahNumber = surahNumber,
                    surahNameAr = effectiveSurahNameAr,
                    surahNameEn = effectiveSurahNameEn
                )

                if (filePath != null) {
                    _event.send(FileDownloadEvent.Finished(filePath))
                    _uiState.update { it.copy(title = "Ready to play") }
                    return@launch
                }
            }

            _uiState.update { it.copy(title = "Getting download URL...") }

            // Store download parameters for retry functionality
            lastDownloadParams = DownloadParams(
                context = context,
                server = server,
                surahNumber = surahNumber,
                reciterName = reciterName,
                surahNameEn = effectiveSurahNameEn,
                surahNameAr = effectiveSurahNameAr
            )

            // Get download URL using the existing use case
            getSurahRecitationUseCase(server, surahNumber.toString())
                .collect { progress ->
                    if (progress.localPath != null) {
                        // We got the download URL, now start the proper download
                        val downloadUrl = progress.localPath!!

                        startDownload(
                            downloadUrl = downloadUrl,
                            reciterName = reciterName,
                            surahNumber = surahNumber,
                            surahNameAr = effectiveSurahNameAr,
                            surahNameEn = effectiveSurahNameEn,
                            serverUrl = server
                        )
                        return@collect
                    }
                }

        } catch (e: Exception) {
            Log.e("ReciterSurahRecitation", "Failed to start download", e)
            _event.send(FileDownloadEvent.Error("Failed to start download: ${e.message}"))
            _uiState.update { it.copy(title = "Download failed") }
        }
    }

    private fun startDownload(
        downloadUrl: String,
        reciterName: String,
        surahNumber: Int,
        surahNameAr: String?,
        surahNameEn: String?,
        serverUrl: String
    ) {
        downloadJob = viewModelScope.launch {
            downloadSurahUseCase(
                downloadUrl = downloadUrl,
                reciterName = reciterName,
                surahNumber = surahNumber,
                surahNameAr = surahNameAr,
                surahNameEn = surahNameEn,
                serverUrl = serverUrl
            ).collect { status ->
                when (status) {
                    is DownloadStatus.Starting -> {
                        _uiState.update { it.copy(title = "Download starting...") }
                    }

                    is DownloadStatus.InProgress -> {
                        val progressPercent = (status.progress * 100).toInt()
                        _uiState.update {
                            it.copy(
                                fileSize = status.totalBytes,
                                title = "Downloading $progressPercent%"
                            )
                        }
                        _event.send(FileDownloadEvent.InProgress(status.progress))
                    }

                    is DownloadStatus.Completed -> {
                        _uiState.update { it.copy(title = "Download completed") }
                        _event.send(FileDownloadEvent.Finished(status.filePath))
                    }

                    is DownloadStatus.Failed -> {
                        _uiState.update { it.copy(title = "Download failed") }
                        _event.send(FileDownloadEvent.Error(status.error))
                    }

                    is DownloadStatus.Cancelled -> {
                        _uiState.update { it.copy(title = "Download cancelled") }
                        _event.send(FileDownloadEvent.Cancelled)
                    }
                }
            }
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
        downloadSurahUseCase.cancelDownload()
    }

    fun retryDownload() {
        lastDownloadParams?.let { params ->
            fetchRecitation(
                context = params.context,
                server = params.server,
                surahNumber = params.surahNumber,
                reciterName = params.reciterName,
                surahNameEn = params.surahNameEn,
                surahNameAr = params.surahNameAr
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
                    currentPosition = current,
                    isPrepared = audio.path.isNotEmpty()
                )
            }.collect { audioPlayerState ->
                _uiState.update { it.copy(audioPlayerState = audioPlayerState) }
            }
        }
    }

    fun sendEvent(context: Context, audioEvent: AudioPlayerAction) {
        val intent = Intent(context, AudioPlayerService::class.java)

        when (audioEvent) {
            is AudioPlayerAction.LoadAudioPlayer -> {
                intent.action = "audio-load"
                intent.putExtra("AUDIO_PATH", audioEvent.url)
                // Extract title from current surah
                val title = _uiState.value.currentSurah?.let { surah ->
                    "${surah.id}. ${surah.name}"
                } ?: "Quran Recitation"
                intent.putExtra("AUDIO_TITLE", title)

                // Add reciter and Surah information
                val reciter = lastDownloadParams?.reciterName ?: "Unknown Reciter"
                val surahInfo = _uiState.value.currentSurah?.let { surah ->
                    "Surah ${surah.id}: ${surah.name}"
                } ?: "Surah Recitation"

                intent.putExtra("AUDIO_RECITER", reciter)
                intent.putExtra("AUDIO_SURAH_INFO", surahInfo)

                startAudioService(context, intent)
            }

            is AudioPlayerAction.SeekTo -> {
                intent.action = "seek-to"
                intent.putExtra("SEEK_POSITION", audioEvent.ms)
                startAudioService(context, intent)
            }

            AudioPlayerAction.PlayPause -> {
                intent.action = "play-pause"
                startAudioService(context, intent)
            }

            AudioPlayerAction.FastForward -> {
                intent.action = "forward"
                startAudioService(context, intent)
            }

            AudioPlayerAction.FastRewind -> {
                intent.action = "rewind"
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
        downloadJob?.cancel()
        downloadSurahUseCase.cancelDownload()
        audioService?.stopPlayback()
        audioService = null
        _uiState.value = ReciterSurahRecitationUiState()
        _event.close()
    }
}
