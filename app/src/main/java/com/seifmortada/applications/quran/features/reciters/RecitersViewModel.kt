package com.seifmortada.applications.quran.features.reciters


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NetworkResult
import com.example.domain.model.ReciterModel
import com.example.domain.usecase.GetAllRecitersUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering
import com.seifmortada.applications.quran.utils.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReciterScreenState(
    val reciters: List<ReciterModel>? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)

class RecitersViewModel(private val getAllRecitersUseCase: GetAllRecitersUseCase) : ViewModel() {


    private var _recitersState = MutableStateFlow<NetworkResult<List<ReciterModel>>?>(null)
    private var _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ReciterScreenState> =
        combine(_recitersState, _searchQuery) { recitersState, searchQuery ->
            when (recitersState) {
                is NetworkResult.Error -> ReciterScreenState(
                    isLoading = false,
                    error = recitersState.errorMessage
                )

                NetworkResult.Loading -> ReciterScreenState(isLoading = true)
                is NetworkResult.Success -> {
                    val filteredReciters = if (searchQuery.isNotBlank()) {
                        recitersState.data.filter { reciter ->
                            reciter.name.contains(searchQuery, ignoreCase = true)
                        }
                    } else recitersState.data
                    ReciterScreenState(
                        isLoading = false,
                        error = null,
                        reciters = filteredReciters
                    )
                }

                null -> TODO()
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ReciterScreenState()
        )

    init {
        fetchAllReciters()
    }

    private fun fetchAllReciters() =
        viewModelScope.launch {
            _recitersState.value = NetworkResult.Loading
            _recitersState.value = getAllRecitersUseCase()
        }


    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = normalizeTextForFiltering(query)
    }
}
