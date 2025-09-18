package com.seifmortada.applications.quran.features.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seifmortada.applications.quran.features.settings.presentation.SettingsRoute
import kotlinx.serialization.Serializable

/**
 * Navigation routes and utilities for Settings feature module
 */
@Serializable
object Settings

/**
 * Extension function to add settings navigation to the navigation graph
 */
fun NavGraphBuilder.settingsSection() {
    composable<Settings> {
        SettingsRoute()
    }
}
