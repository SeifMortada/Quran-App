package com.seifmortada.applications.quran.features.surah_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NetworkResult
import com.example.domain.model.SurahModel
import com.example.domain.usecase.FetchAyahRecitationUseCase
import com.example.domain.usecase.GetSurahByIdUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.calculateGlobalAyahNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SurahState(
    val surah: SurahModel? = null,
    val error: String? = null,
    val loading: Boolean = false,
    val searchQuery: String = "",
    val surahAudioUrl: String? = null
)

class SurahViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val fetchAyahRecitationUseCase: FetchAyahRecitationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SurahState())
    val uiState = _uiState.asStateFlow()

    fun getAyahRecitation(surahNumber: String, ayahNumber: String) =
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                val globalAyahNumber = calculateGlobalAyahNumber(
                    surahNumber.toInt(),
                    ayahNumber.toInt()
                )
                _uiState.update {
                    when (val response = fetchAyahRecitationUseCase(globalAyahNumber)) {
                        is NetworkResult.Error -> {
                            it.copy(error = response.errorMessage, loading = false)
                        }

                        NetworkResult.Loading -> it.copy(loading = true)
                        is NetworkResult.Success -> it.copy(
                            loading = false,
                            surahAudioUrl = response.data
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, loading = false) }
            }
        }

    fun getSurahById(id: Int) = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        try {
            val surah = getSurahByIdUseCase(id)
            _uiState.update { it.copy(surah = surah, loading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message, loading = false) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filteredVerses = state.surah?.verses?.filter {
                it.text.contains(query, ignoreCase = true)
            } ?: emptyList()

            state.copy(searchQuery = query, surah = state.surah?.copy(verses = filteredVerses))
        }
    }
}