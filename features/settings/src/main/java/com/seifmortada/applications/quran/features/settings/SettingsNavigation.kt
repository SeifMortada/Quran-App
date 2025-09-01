package com.seifmortada.applications.quran.features.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
