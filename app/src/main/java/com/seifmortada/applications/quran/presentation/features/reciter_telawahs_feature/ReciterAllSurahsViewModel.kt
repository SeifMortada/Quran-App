package com.seifmortada.applications.quran.presentation.features.reciter_telawahs_feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.MoshafModel
import com.example.domain.model.SurahModel
import com.example.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReciterAllSurahsViewModel(private val getQuranUseCase: GetQuranUseCase) : ViewModel() {

    private val _surahs = MutableStateFlow<List<SurahModel>>(emptyList())
    val surahs = _surahs.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredSurahs: StateFlow<List<SurahModel>> =
        combine(_surahs, _searchQuery) { surahList, query ->
            if (query.isBlank()) {
                surahList
            } else {
                surahList.filter { it.name.contains(query, ignoreCase = true) }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun filterSurahsWithThisTelawah(telawah: MoshafModel) {
        viewModelScope.launch {
            val allSurahs = getQuranUseCase()
            val filteredSurahs = allSurahs.filter { surah ->
                surah.id in telawah.surahList.split(",")
                    .map { surahsTelawah ->
                        surahsTelawah.toInt()
                    }
            }
            _surahs.update {
                filteredSurahs
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { normalizeTextForFiltering(query) }
    }
}