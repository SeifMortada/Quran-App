package com.seifmortada.applications.quran.features.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("quran_app_settings", Context.MODE_PRIVATE)

    private val _themeFlow = MutableStateFlow(getStoredTheme())
    private val _languageFlow = MutableStateFlow(getStoredLanguage())

    private fun getStoredTheme(): Theme {
        val themeString = sharedPreferences.getString("theme", Theme.SYSTEM.name)
        return try {
            Theme.valueOf(themeString ?: Theme.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            Theme.SYSTEM
        }
    }

    private fun getStoredLanguage(): Language {
        val languageString = sharedPreferences.getString("language", Language.ENGLISH.name)
        return try {
            Language.valueOf(languageString ?: Language.ENGLISH.name)
        } catch (e: IllegalArgumentException) {
            Language.ENGLISH
        }
    }

    suspend fun saveTheme(theme: Theme) {
        sharedPreferences.edit().putString("theme", theme.name).apply()
        _themeFlow.value = theme
    }

    suspend fun saveLanguage(language: Language) {
        sharedPreferences.edit().putString("language", language.name).apply()
        _languageFlow.value = language
    }

    fun getTheme(): Flow<Theme> = _themeFlow.asStateFlow()

    fun getLanguage(): Flow<Language> = _languageFlow.asStateFlow()
}