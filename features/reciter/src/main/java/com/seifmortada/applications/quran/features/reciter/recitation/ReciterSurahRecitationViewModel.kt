package com.seifmortada.applications.quran.features.reciter.recitation

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.core.domain.model.download.*
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahByIdUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.core.domain.usecase.DownloadSurahUseCase
import com.seifmortada.applications.quran.core.service.audio.AudioPlayerService
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

    // Store current download request for better state management
    private var currentDownloadRequest: DownloadRequest? = null

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

            // Create download request for better state management
            val downloadRequest = DownloadRequest(
                downloadUrl = "", // Will be populated when we get the URL
                reciterName = reciterName,
                surahNumber = surahNumber,
                surahNameAr = effectiveSurahNameAr,
                surahNameEn = effectiveSurahNameEn,
                serverUrl = server
            )

            // Check if file is already downloaded using new approach
            if (downloadSurahUseCase.isSurahDownloaded(downloadRequest)) {
                val filePath = downloadSurahUseCase.getSurahFilePath(downloadRequest)
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
/*                    if (progress.progress != null) {
                        // We got the download URL, now start the enhanced download
                        val downloadUrl = progress.localPath!!

                        // Update the download request with the URL
                        currentDownloadRequest = downloadRequest.copy(downloadUrl = downloadUrl)

                        startDownload(currentDownloadRequest!!)
                        return@collect
                    }*/
                }

        } catch (e: Exception) {
            _event.send(FileDownloadEvent.Error("Failed to start download: ${e.message}"))
            _uiState.update { it.copy(title = "Download failed") }
        }
    }

    private fun startDownload(downloadRequest: DownloadRequest) {
        downloadJob = viewModelScope.launch {
            downloadSurahUseCase(downloadRequest).collect { status ->
                when (status) {
                    is DownloadStatus.Idle -> {
                        _uiState.update { it.copy(title = "Preparing download...") }
                    }

                    is DownloadStatus.Starting -> {
                        _uiState.update { it.copy(title = "Download starting...") }
                        _event.send(FileDownloadEvent.Starting)
                    }

                    is DownloadStatus.InProgress -> {
                        val progress = status.downloadProgress
                        val progressPercent = progress.progressPercentage

                        // Enhanced title with speed and time information
                        val speedText = if (progress.downloadSpeed > 0) {
                            " • ${formatDownloadSpeed(progress.downloadSpeed)}"
                        } else ""

                        val timeText = if (progress.estimatedTimeRemaining > 0) {
                            " • ${formatTime(progress.estimatedTimeRemaining)}"
                        } else ""

                        _uiState.update {
                            it.copy(
                                fileSize = progress.totalBytes,
                                title = "Downloading $progressPercent%$speedText$timeText"
                            )
                        }

                        _event.send(
                            FileDownloadEvent.InProgress(
                                progress = progress.progress,
                                downloadedBytes = progress.downloadedBytes,
                                totalBytes = progress.totalBytes,
                                downloadSpeed = progress.downloadSpeed,
                                estimatedTimeRemaining = progress.estimatedTimeRemaining
                            )
                        )
                    }

                    is DownloadStatus.Completed -> {
                        _uiState.update { it.copy(title = "Download completed") }
                        _event.send(FileDownloadEvent.Finished(status.filePath))
                    }

                    is DownloadStatus.Failed -> {
                        val errorMessage = "${status.error} (${status.errorCode})"
                        _uiState.update { it.copy(title = "Download failed") }
                        _event.send(FileDownloadEvent.Error(errorMessage))
                    }

                    is DownloadStatus.Cancelled -> {
                        _uiState.update { it.copy(title = "Download cancelled") }
                        _event.send(FileDownloadEvent.Cancelled)
                    }

                    is DownloadStatus.Paused -> {
                        _uiState.update { it.copy(title = "Download paused") }
                        // Could add a paused event if needed
                    }
                }
            }
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()

        currentDownloadRequest?.let { request ->
            downloadSurahUseCase.cancelDownload(request.downloadId)
        } ?: run {
            downloadSurahUseCase.cancelDownload() // Fallback to cancel all
        }
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

    private fun formatDownloadSpeed(bytesPerSecond: Long): String {
        return when {
            bytesPerSecond < 1024 -> "${bytesPerSecond}B/s"
            bytesPerSecond < 1024 * 1024 -> "${bytesPerSecond / 1024}KB/s"
            else -> "${bytesPerSecond / (1024 * 1024)}MB/s"
        }
    }

    private fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return if (minutes > 0) {
            "${minutes}m ${remainingSeconds}s"
        } else {
            "${remainingSeconds}s"
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanResources()
    }

    private fun cleanResources() {
        serviceCollectJob?.cancel()
        downloadJob?.cancel()

        // Only try to cancel download if there's an active download job
        if (downloadJob?.isActive == true) {
            try {
                downloadSurahUseCase.cancelDownload()
            } catch (e: Exception) {
                // Ignore cancellation errors during cleanup
            }
        }

        audioService?.stopPlayback()
        audioService = null
        currentDownloadRequest = null
        _uiState.value = ReciterSurahRecitationUiState()
        _event.close()
    }
}
