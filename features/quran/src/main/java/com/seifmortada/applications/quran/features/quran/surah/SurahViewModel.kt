package com.seifmortada.applications.quran.features.quran.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.core.domain.usecase.FetchAyahRecitationUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahByIdUseCase
import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.ui.utils.FunctionsUtils.calculateGlobalAyahNumber
import com.seifmortada.applications.quran.core.ui.utils.FunctionsUtils.normalizeTextForFiltering
import com.seifmortada.applications.quran.core.ui.utils.WhileUiSubscribed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SurahUiState(
    val surah: SurahModel? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val surahAudioUrl: String? = null
)

class SurahViewModel(
    private val getSurahByIdUseCase: GetSurahByIdUseCase,
    private val fetchAyahRecitationUseCase: FetchAyahRecitationUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _surahData = MutableStateFlow<SurahModel?>(null)

    private val debouncedSearchQuery = _searchQuery.debounce(300).distinctUntilChanged()

    private val _filteredSurah = combine(_surahData, debouncedSearchQuery) { surah, searchQuery ->
        withContext(Dispatchers.Default) {
            if (searchQuery.isBlank()) surah else {
                surah?.let {
                    val filteredVerses = it.verses.filter { ayah ->
                        normalizeTextForFiltering(ayah.text).contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }
                    it.copy(verses = filteredVerses)
                }
            }
        }
    }.stateIn(viewModelScope, WhileUiSubscribed, null)

    private val _uiState = MutableStateFlow(SurahUiState())
    val uiState = _uiState.combine(_filteredSurah) { uiState, filteredSurah ->
        uiState.copy(surah = filteredSurah)
    }.stateIn(viewModelScope, WhileUiSubscribed, SurahUiState())


    fun getSurahById(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                _surahData.update {getSurahByIdUseCase(id) }
            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = e.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    suspend fun getAyahAudioUrl(surahNumber: Int, ayahNumber: Int): String? {
        return try {
            val globalAyahNumber = calculateGlobalAyahNumber(surahNumber, ayahNumber)
            when (val response = fetchAyahRecitationUseCase(globalAyahNumber)) {
                is NetworkResult.Success -> response.data
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }
}
