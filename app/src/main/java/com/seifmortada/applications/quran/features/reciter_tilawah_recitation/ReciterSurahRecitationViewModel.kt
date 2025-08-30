package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.core.service.AudioPlayerService
import com.seifmortada.applications.quran.core.service.DownloadHelper
import com.seifmortada.applications.quran.core.service.DownloadService
import com.seifmortada.applications.quran.core.service.FORWARD
import com.seifmortada.applications.quran.core.service.PLAYPAUSE
import com.seifmortada.applications.quran.core.service.REWIND
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
    private val getSurahRecitationUseCase: GetSurahRecitationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReciterSurahRecitationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<FileDownloadEvent>()
    val event = _event.receiveAsFlow()

    private var audioService: AudioPlayerService? = null
    private var serviceCollectJob: Job? = null

    private var downloadBroadcastReceiver: BroadcastReceiver? = null
    private var localBroadcastManager: LocalBroadcastManager? = null

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
            val existingFilePath = DownloadHelper.getSurahFilePath(
                context,
                reciterName,
                server,
                surahNumber,
                effectiveSurahNameAr,
                effectiveSurahNameEn
            )

            if (existingFilePath != null) {
                _event.send(FileDownloadEvent.Finished(existingFilePath))
                _uiState.update {
                    it.copy(title = "Ready to play")
                }
                return@launch
            }
            
            _uiState.update {
                it.copy(title = "Getting download URL...")
            }

            setupDownloadBroadcastReceiver(context)

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

                        val success = DownloadHelper.startSurahDownload(
                            context = context,
                            downloadUrl = downloadUrl,
                            reciterName = reciterName,
                            surahNumber = surahNumber,
                            surahNameAr = effectiveSurahNameAr,
                            surahNameEn = effectiveSurahNameEn,
                            serverUrl = server
                        )

                        if (success) {
                            _uiState.update {
                                it.copy(title = "Download starting...")
                            }
                        } else {
                            _event.send(FileDownloadEvent.Error("Could not start download. Check permissions."))
                            _uiState.update { it.copy(title = "Download failed - check permissions") }
                            cleanupBroadcastReceiver()
                        }
                        return@collect
                    }
                }

        } catch (e: Exception) {
            Log.e("ReciterSurahRecitation", "Failed to start download", e)
            _event.send(FileDownloadEvent.Error("Failed to start download: ${e.message}"))
            _uiState.update { it.copy(title = "Download failed") }
            cleanupBroadcastReceiver()
        }
    }

    private fun setupDownloadBroadcastReceiver(context: Context) {
        downloadBroadcastReceiver?.let { receiver ->
            localBroadcastManager?.unregisterReceiver(receiver)
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(context)

        downloadBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    DownloadService.BROADCAST_DOWNLOAD_PROGRESS -> {
                        val progress = intent.getFloatExtra(DownloadService.EXTRA_PROGRESS, 0f)
                        val downloadedBytes =
                            intent.getLongExtra(DownloadService.EXTRA_DOWNLOADED_BYTES, 0L)
                        val totalBytes = intent.getLongExtra(DownloadService.EXTRA_TOTAL_BYTES, 0L)

                        val progressPercent = (progress * 100).toInt()
                        _uiState.update {
                            it.copy(
                                fileSize = totalBytes,
                                title = "Downloading $progressPercent%"
                            )
                        }

                        viewModelScope.launch {
                            _event.send(FileDownloadEvent.InProgress(progress))
                        }
                    }

                    DownloadService.BROADCAST_DOWNLOAD_COMPLETED -> {
                        val filePath = intent.getStringExtra(DownloadService.EXTRA_FILE_PATH)

                        _uiState.update {
                            it.copy(title = "Download completed")
                        }

                        viewModelScope.launch {
                            _event.send(FileDownloadEvent.Finished(filePath ?: ""))
                        }
                        
                        cleanupBroadcastReceiver()
                    }

                    DownloadService.BROADCAST_DOWNLOAD_FAILED -> {
                        val errorMessage =
                            intent.getStringExtra(DownloadService.EXTRA_ERROR_MESSAGE)

                        _uiState.update {
                            it.copy(title = "Download failed")
                        }

                        viewModelScope.launch {
                            _event.send(FileDownloadEvent.Error(errorMessage ?: "Download failed"))
                        }
                        
                        cleanupBroadcastReceiver()
                    }

                    DownloadService.BROADCAST_DOWNLOAD_CANCELLED -> {
                        _uiState.update {
                            it.copy(title = "Download cancelled")
                        }

                        viewModelScope.launch {
                            _event.send(FileDownloadEvent.Cancelled)
                        }

                        cleanupBroadcastReceiver()
                    }
                }
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction(DownloadService.BROADCAST_DOWNLOAD_PROGRESS)
            addAction(DownloadService.BROADCAST_DOWNLOAD_COMPLETED)
            addAction(DownloadService.BROADCAST_DOWNLOAD_FAILED)
            addAction(DownloadService.BROADCAST_DOWNLOAD_CANCELLED)
        }

        localBroadcastManager?.registerReceiver(downloadBroadcastReceiver!!, intentFilter)
    }

    private fun cleanupBroadcastReceiver() {
        downloadBroadcastReceiver?.let { receiver ->
            localBroadcastManager?.unregisterReceiver(receiver)
            downloadBroadcastReceiver = null
        }
        localBroadcastManager = null
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
                intent.action = com.seifmortada.applications.quran.core.service.AUDIO_LOAD
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
        cleanupBroadcastReceiver()
        _uiState.value = ReciterSurahRecitationUiState()
        _event.close()
    }
}
