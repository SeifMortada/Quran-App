package com.seifmortada.applications.quran.core.navigation.destinations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.seifmortada.applications.quran.features.quran.QuranChapters
import com.seifmortada.applications.quran.features.reciter.Reciters
import com.seifmortada.applications.quran.features.settings.Settings

data class TopLevelDestination<T : Any>(
    val titleRes: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: T
)

val topLevelDestinations = listOf(
    TopLevelDestination(
        titleRes = com.seifmortada.applications.quran.R.string.home,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        route = Home
    ),
    TopLevelDestination(
        titleRes = com.seifmortada.applications.quran.R.string.quran,
        selectedIcon = Icons.Filled.Book,
        unSelectedIcon = Icons.Outlined.Book,
        route = QuranChapters
    ),
    TopLevelDestination(
        titleRes = com.seifmortada.applications.quran.R.string.quran_readers,
        selectedIcon = Icons.Filled.RecordVoiceOver,
        unSelectedIcon = Icons.Outlined.RecordVoiceOver,
        route = Reciters
    ),
    TopLevelDestination(
        titleRes = com.seifmortada.applications.quran.R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
        route = Settings
    )
)
