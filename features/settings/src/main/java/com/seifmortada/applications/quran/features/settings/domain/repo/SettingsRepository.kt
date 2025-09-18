package com.seifmortada.applications.quran.features.settings.domain.repo

import com.seifmortada.applications.quran.features.settings.domain.model.Language
import com.seifmortada.applications.quran.features.settings.domain.model.Theme
import kotlinx.coroutines.flow.Flow

// Re-export the core domain SettingsRepository for feature-internal use
typealias SettingsRepository = com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository