package com.seifmortada.applications.quran.core.data.repository.settings

import com.seifmortada.applications.quran.core.domain.model.settings.Language
import com.seifmortada.applications.quran.core.domain.model.settings.Theme
import android.content.Context
import android.content.SharedPreferences
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedPreferencesSettingsRepository(context: Context) : SettingsRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("quran_app_settings", Context.MODE_PRIVATE)

    private val _themeFlow = MutableStateFlow(getStoredTheme())
    private val _languageFlow = MutableStateFlow(getStoredLanguage())

    private fun getStoredTheme(): Theme {
        val themeString = sharedPreferences.getString("theme", Theme.SYSTEM.name)
        return try {
            Theme.valueOf(themeString ?: Theme.SYSTEM.name)
        } catch (_: IllegalArgumentException) {
            Theme.SYSTEM
        }
    }

    private fun getStoredLanguage(): Language {
        val languageString = sharedPreferences.getString("language", Language.ENGLISH.name)
        return try {
            Language.valueOf(languageString ?: Language.ENGLISH.name)
        } catch (_: IllegalArgumentException) {
            Language.ENGLISH
        }
    }

    override suspend fun saveTheme(theme: Theme) {
        sharedPreferences.edit().putString("theme", theme.name).apply()
        _themeFlow.value = theme
    }

    override suspend fun saveLanguage(language: Language) {
        sharedPreferences.edit().putString("language", language.name).apply()
        _languageFlow.value = language
    }

    override fun getTheme(): Flow<Theme> = _themeFlow.asStateFlow()

    override fun getLanguage(): Flow<Language> = _languageFlow.asStateFlow()
}
