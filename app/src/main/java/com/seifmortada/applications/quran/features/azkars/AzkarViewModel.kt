package com.seifmortada.applications.quran.features.azkars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AzkarModel
import com.example.domain.usecase.GetAzkarsUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering
import com.seifmortada.applications.quran.utils.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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