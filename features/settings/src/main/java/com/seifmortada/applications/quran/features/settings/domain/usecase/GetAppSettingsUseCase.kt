package com.seifmortada.applications.quran.features.settings.domain.usecase

import com.seifmortada.applications.quran.features.settings.domain.repo.SettingsRepository
import com.seifmortada.applications.quran.features.settings.domain.model.AppSettings
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