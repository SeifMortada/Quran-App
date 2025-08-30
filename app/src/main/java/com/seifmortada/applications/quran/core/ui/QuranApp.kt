package com.seifmortada.applications.quran.core.ui

import android.text.TextUtils
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.text.layoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.seifmortada.applications.quran.core.navigation.QuranAppNavGraph
import com.seifmortada.applications.quran.core.navigation.destinations.QuranScreens
import com.seifmortada.applications.quran.core.navigation.destinations.topLevelDestinations
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import java.util.Locale

@Composable
fun QuranApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)
    var selectedItemIndex = when (currentBackStack?.destination?.route) {
        QuranScreens.Home::class.simpleName -> 0
        QuranScreens.QuranChapters::class.simpleName -> 1
        QuranScreens.Reciters::class.simpleName -> 2
        QuranScreens.Settings::class.simpleName -> 3
        else -> -1
    }
    val context = LocalContext.current
    val isRtl = Locale.getDefault().layoutDirection == android.util.LayoutDirection.RTL

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    topLevelDestinations.forEachIndexed { index, topLevelDestination ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(topLevelDestination.route)
                            },
                            label = {
                                Text(
                                    text = context.getString(topLevelDestination.titleRes),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selectedItemIndex == index)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) topLevelDestination.selectedIcon else topLevelDestination.unSelectedIcon,
                                    contentDescription = context.getString(topLevelDestination.titleRes),
                                    tint = if (selectedItemIndex == index)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                QuranAppNavGraph(navController = navController)
            }
        }
    }
}