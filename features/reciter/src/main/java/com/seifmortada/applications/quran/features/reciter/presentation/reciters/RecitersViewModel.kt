package com.seifmortada.applications.quran.features.reciter.presentation.reciters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.features.reciter.domain.usecase.GetAllRecitersUseCase
import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.core.ui.utils.FunctionsUtils.normalizeTextForFiltering
import com.seifmortada.applications.quran.core.ui.utils.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReciterScreenState(
    val reciters: List<ReciterModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)

class RecitersViewModel(
    private val getAllRecitersUseCase: GetAllRecitersUseCase
) : ViewModel() {

    private val _recitersState = MutableStateFlow<List<ReciterModel>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ReciterScreenState> =
        combine(
            _recitersState,
            _searchQuery,
            _isLoading,
            _error
        ) { reciters, searchQuery, isLoading, error ->
            val filteredReciters = if (searchQuery.isNotBlank()) {
                reciters.filter { reciter ->
                    reciter.name.contains(searchQuery, ignoreCase = true)
                }
            } else reciters

            ReciterScreenState(
                reciters = filteredReciters,
                isLoading = isLoading,
                error = error,
                searchQuery = searchQuery
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ReciterScreenState()
        )

    init {
        fetchAllReciters()
    }

    private fun fetchAllReciters() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        val result = getAllRecitersUseCase()
        _isLoading.value = false

        if (result is NetworkResult.Success) {
            _recitersState.value = sortRecitersByName(result.data)
        }
    }

    private fun sortRecitersByName(reciters: List<ReciterModel>): List<ReciterModel> {
        return reciters.sortedBy { it.name }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = normalizeTextForFiltering(query)
    }
}
