package com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NetworkResult
import com.example.domain.model.SurahModel
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SurahRecitationState(
    val audioUrl: String = "",
    val title: String = "",
    val currentSurah: SurahModel? = null,
    val isError: String = "",
    val isLoading: Boolean = false
)

class SurahRecitationViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val getSurahRecitationUseCase: GetSurahRecitationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SurahRecitationState())
    val uiState = _uiState.asStateFlow()

    fun fetchRecitation(server: String, surahNumber: Int) =
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentSurah = getSurahByIdUseCase(surahNumber)
            _uiState.update { it.copy(currentSurah = currentSurah) }
            val surahRecitationResponse = getSurahRecitationUseCase(server, surahNumber.toString())
            when (surahRecitationResponse) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            audioUrl = surahRecitationResponse.data,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        isError = surahRecitationResponse.errorMessage,
                        isLoading = false
                    )
                }

                else -> Unit
            }
        }
}