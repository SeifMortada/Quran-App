package com.seifmortada.applications.quran.core.navigation.destinations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.seifmortada.applications.quran.R

data class TopLevelDestination<T : Any>(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: T
)

val topLevelDestinations = listOf(
    TopLevelDestination(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        route = QuranScreens.Home
    ),
    TopLevelDestination(
        title = "Quran Reciters",
        selectedIcon = Icons.Filled.Mic,
        unSelectedIcon = Icons.Outlined.Mic,
        route = QuranScreens.Reciters
    ),
    TopLevelDestination(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
        route = QuranScreens.Settings
    )
)