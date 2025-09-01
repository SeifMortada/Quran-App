package com.seifmortada.applications.quran.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.seifmortada.applications.quran.core.domain.model.settings.Theme
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import org.koin.androidx.compose.get

@Composable
fun QuranAppThemeProvider(
    settingsRepository: SettingsRepository = get(),
    content: @Composable () -> Unit
) {
    val theme by settingsRepository.getTheme().collectAsState(initial = Theme.SYSTEM)
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val isDarkTheme = when (theme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM -> isSystemInDarkTheme
    }

    QuranAppTheme(
        darkTheme = isDarkTheme,
        content = content
    )
}
