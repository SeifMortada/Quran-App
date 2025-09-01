package com.seifmortada.applications.quran.core.domain.repository.settings

import com.seifmortada.applications.quran.core.domain.model.settings.Language
import com.seifmortada.applications.quran.core.domain.model.settings.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveTheme(theme: Theme)
    suspend fun saveLanguage(language: Language)
    fun getTheme(): Flow<Theme>
    fun getLanguage(): Flow<Language>
}
