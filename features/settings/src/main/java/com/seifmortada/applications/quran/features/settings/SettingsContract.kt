package com.seifmortada.applications.quran.features.settings

import com.seifmortada.applications.quran.core.domain.model.settings.Language
import com.seifmortada.applications.quran.core.domain.model.settings.Theme

/**
 * MVI Contract for Settings Feature
 */
object SettingsContract {

    data class State(
        val theme: Theme = Theme.SYSTEM,
        val language: Language = Language.ENGLISH,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showFeedbackDialog: Boolean = false,
        val showSupportDialog: Boolean = false
    )

    sealed class Intent {
        data class UpdateTheme(val theme: Theme) : Intent()
        data class UpdateLanguage(val language: Language) : Intent()
        data class SendFeedback(val feedback: String) : Intent()
        object ShowFeedbackDialog : Intent()
        object HideFeedbackDialog : Intent()
        object ShowSupportDialog : Intent()
        object HideSupportDialog : Intent()
        data class PurchaseProduct(val productId: String) : Intent()
        object LoadSettings : Intent()
    }

    sealed class Effect {
        data class ShowToast(val message: String) : Effect()
        object FeedbackSent : Effect()
        data class NavigateToEmail(val emailData: EmailData) : Effect()
        data class PurchaseSuccess(val productId: String) : Effect()
        data class PurchaseError(val error: String) : Effect()
    }

    data class EmailData(
        val subject: String,
        val body: String,
        val recipients: List<String>
    )
}
