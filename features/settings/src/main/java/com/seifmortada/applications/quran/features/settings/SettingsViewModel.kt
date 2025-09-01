package com.seifmortada.applications.quran.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import com.seifmortada.applications.quran.core.domain.model.settings.Language
import com.seifmortada.applications.quran.core.domain.model.settings.Theme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsContract.State())
    val state: StateFlow<SettingsContract.State> =
        combine(
            _state,
            settingsRepository.getTheme(),
            settingsRepository.getLanguage()
        ) { currentState, theme, language ->
            currentState.copy(
                theme = theme,
                language = language,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsContract.State(isLoading = true)
        )

    private val _effect = Channel<SettingsContract.Effect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: SettingsContract.Intent) {
        when (intent) {
            is SettingsContract.Intent.UpdateTheme -> updateTheme(intent.theme)
            is SettingsContract.Intent.UpdateLanguage -> updateLanguage(intent.language)
            is SettingsContract.Intent.SendFeedback -> sendFeedback(intent.feedback)
            is SettingsContract.Intent.ShowFeedbackDialog -> showFeedbackDialog()
            is SettingsContract.Intent.HideFeedbackDialog -> hideFeedbackDialog()
            is SettingsContract.Intent.ShowSupportDialog -> showSupportDialog()
            is SettingsContract.Intent.HideSupportDialog -> hideSupportDialog()
            is SettingsContract.Intent.PurchaseProduct -> purchaseProduct(intent.productId)
            is SettingsContract.Intent.LoadSettings -> loadSettings()
        }
    }

    private fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            try {
                settingsRepository.saveTheme(theme)
                _effect.send(SettingsContract.Effect.ShowToast("Theme updated successfully"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to update theme") }
            }
        }
    }

    private fun updateLanguage(language: Language) {
        viewModelScope.launch {
            try {
                settingsRepository.saveLanguage(language)
                _effect.send(SettingsContract.Effect.ShowToast("Language updated successfully"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to update language") }
            }
        }
    }

    private fun sendFeedback(feedback: String) {
        viewModelScope.launch {
            val emailData = SettingsContract.EmailData(
                subject = "Quran App Feedback",
                body = feedback,
                recipients = listOf("developer@quranapp.com")
            )
            _effect.send(SettingsContract.Effect.NavigateToEmail(emailData))
        }
    }

    private fun showFeedbackDialog() {
        _state.update { it.copy(showFeedbackDialog = true) }
    }

    private fun hideFeedbackDialog() {
        _state.update { it.copy(showFeedbackDialog = false) }
    }

    private fun showSupportDialog() {
        _state.update { it.copy(showSupportDialog = true) }
    }

    private fun hideSupportDialog() {
        _state.update { it.copy(showSupportDialog = false) }
    }

    private fun purchaseProduct(productId: String) {
        viewModelScope.launch {
            // This will be handled by the billing manager
            _effect.send(SettingsContract.Effect.ShowToast("Processing purchase for $productId"))
        }
    }

    private fun loadSettings() {
        // Settings are automatically loaded via the combine flow
        _state.update { it.copy(isLoading = false) }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}

// Legacy methods for backward compatibility - can be removed after migration
fun SettingsViewModel.updateTheme(theme: Theme) =
    handleIntent(SettingsContract.Intent.UpdateTheme(theme))

fun SettingsViewModel.updateLanguage(language: Language) =
    handleIntent(SettingsContract.Intent.UpdateLanguage(language))

fun SettingsViewModel.sendFeedback(feedback: String) =
    handleIntent(SettingsContract.Intent.SendFeedback(feedback))

data class SettingsUiState(
    val theme: Theme = Theme.SYSTEM,
    val language: Language = Language.ENGLISH,
    val isLoading: Boolean = false
)
