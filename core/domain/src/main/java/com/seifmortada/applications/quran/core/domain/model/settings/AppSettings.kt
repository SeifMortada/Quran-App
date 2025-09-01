package com.seifmortada.applications.quran.core.domain.model.settings

data class AppSettings(
    val theme: Theme = Theme.SYSTEM,
    val language: Language = Language.ENGLISH
)
