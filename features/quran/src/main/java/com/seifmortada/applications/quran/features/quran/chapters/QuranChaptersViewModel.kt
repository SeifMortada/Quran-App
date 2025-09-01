package com.seifmortada.applications.quran.features.quran.chapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.core.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.ui.utils.FunctionsUtils.normalizeTextForFiltering
import com.seifmortada.applications.quran.core.ui.utils.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuranChaptersViewModel(private val getQuranUseCase: GetQuranUseCase) : ViewModel() {
    private val _chaptersState = MutableStateFlow<List<SurahModel>?>(null)
    private var _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<List<SurahModel>> =
        combine(_chaptersState, _searchQuery) { chaptersState, searchQuery ->
            if (searchQuery.isBlank())
                chaptersState ?: emptyList()
            else
                chaptersState?.filter { it.name.contains(searchQuery) } ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    init {
        getQuranChapters()
    }

    private fun getQuranChapters() = viewModelScope.launch {
        _chaptersState.update { getQuranUseCase() }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = normalizeTextForFiltering(query)
    }
}
