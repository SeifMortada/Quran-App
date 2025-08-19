package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SurahModel
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SurahRecitationState(
    val audioUrl: String = "",
    val fileSize: Long = 0L,
    val title: String = "",
    val currentSurah: SurahModel? = null,
    val isError: String = "",
    val isLoading: Boolean = false,
    val downloadState: DownloadState = DownloadState.Idle

)

class SurahRecitationViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val getSurahRecitationUseCase: GetSurahRecitationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SurahRecitationState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    fun fetchRecitation(server: String, surahNumber: Int) = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentSurah = getSurahByIdUseCase(surahNumber)
            _uiState.update { it.copy(currentSurah = currentSurah, isLoading = false) }
            getSurahRecitationUseCase(server, surahNumber.toString())
                .collect { progress ->
                    val clamped = (progress.progress).coerceIn(0f, 1f)
                    _uiState.update {
                        it.copy(
                            fileSize = progress.totalBytes,
                            isLoading = false,
                            title = "Downloading ${(clamped * 100).toInt()}%",
                            downloadState = if (clamped < 1f) {
                                DownloadState.InProgress(clamped)
                            } else {
                                DownloadState.Finished(progress.localPath.toString())
                            }
                        )
                    }
                }
        }


    fun searchQuery(query: String) {
        _searchQuery.value = normalizeTextForFiltering(query)
    }
}