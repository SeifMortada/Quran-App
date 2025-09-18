package com.seifmortada.applications.quran.features.zikr.presentation.azkars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.ui.utils.FunctionsUtils.normalizeTextForFiltering
import com.seifmortada.applications.quran.core.ui.utils.WhileUiSubscribed
import com.seifmortada.applications.quran.features.zikr.domain.usecase.GetAzkarsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.filter

class AzkarViewModel(private val getAzkarsUseCase: GetAzkarsUseCase) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _azkarsList = MutableStateFlow<List<AzkarModel>>(emptyList())

    val uiState: StateFlow<List<AzkarModel>> =
        combine(_searchQuery, _azkarsList) { searchQuery, azkarsList ->
            if (searchQuery.isBlank()) {
                azkarsList
            } else {
                azkarsList.filter { azkar ->
                    azkar.category.contains(searchQuery, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    init {
        getAzkars()
    }

    private fun getAzkars() = viewModelScope.launch {
        _azkarsList.update {
            getAzkarsUseCase()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = normalizeTextForFiltering(query)
    }

}
