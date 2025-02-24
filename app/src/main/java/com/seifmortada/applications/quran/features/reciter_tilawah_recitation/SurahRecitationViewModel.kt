package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NetworkResult
import com.example.domain.model.SurahModel
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SurahRecitationState(
    val audioUrl: String = "",
    val fileSize: Long = 0L,
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

    private val _searchQuery = MutableStateFlow("")

    fun fetchRecitation(server: String, surahNumber: Int) =
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentSurah = getSurahByIdUseCase(surahNumber)
            _uiState.update { it.copy(currentSurah = currentSurah) }
            val surahRecitationResponse = getSurahRecitationUseCase(server, surahNumber.toString())
            when (surahRecitationResponse) {
                is NetworkResult.Success -> {
                    val (audioUrl, fileSize) = surahRecitationResponse.data
                    _uiState.update {
                        it.copy(
                            audioUrl = audioUrl,
                            fileSize = fileSize,
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

    fun searchQuery(query: String) {
        _searchQuery.value = normalizeTextForFiltering(query)
    }
}