package com.seifmortada.applications.quran.core.ui.locale

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import com.seifmortada.applications.quran.core.domain.model.settings.Language as AppLanguage
import org.koin.androidx.compose.get
import java.util.Locale

@Composable
fun LocaleProvider(
    settingsRepository: SettingsRepository = get(),
    content: @Composable () -> Unit
) {
    val language by settingsRepository.getLanguage().collectAsState(initial = AppLanguage.ENGLISH)
    val context = LocalContext.current

    // Update app locale based on selected language
    val locale = when (language) {
        AppLanguage.ENGLISH -> Locale.ENGLISH
        AppLanguage.ARABIC -> Locale("ar")
    }

    // Apply locale to the context
    updateContextLocale(context, locale)

    content()
}

private fun updateContextLocale(context: Context, locale: Locale) {
    val config = context.resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
