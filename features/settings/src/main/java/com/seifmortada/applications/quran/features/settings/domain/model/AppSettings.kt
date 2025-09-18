package com.seifmortada.applications.quran.features.settings.domain.model

data class AppSettings(
    val theme: Theme = Theme.SYSTEM,
    val language: Language = Language.ENGLISH
)
