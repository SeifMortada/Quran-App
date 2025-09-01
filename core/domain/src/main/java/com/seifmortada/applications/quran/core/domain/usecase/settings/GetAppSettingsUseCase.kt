package com.seifmortada.applications.quran.core.domain.usecase.settings

import com.seifmortada.applications.quran.core.domain.model.settings.AppSettings
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAppSettingsUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<AppSettings> {
        return combine(
            settingsRepository.getTheme(),
            settingsRepository.getLanguage()
        ) { theme, language ->
            AppSettings(
                theme = theme,
                language = language
            )
        }
    }
}
