package com.seifmortada.applications.quran.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getTheme().collect { theme ->
                _uiState.value = _uiState.value.copy(theme = theme)
            }
        }

        viewModelScope.launch {
            settingsRepository.getLanguage().collect { language ->
                _uiState.value = _uiState.value.copy(language = language)
            }
        }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingsRepository.saveTheme(theme)
        }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch {
            settingsRepository.saveLanguage(language)
        }
    }

    fun sendFeedback(feedback: String) {
        // This is handled by the UI through the email intent
        // We could log analytics here if needed
    }
}