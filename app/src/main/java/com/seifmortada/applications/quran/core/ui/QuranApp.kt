package com.seifmortada.applications.quran.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.seifmortada.applications.quran.core.navigation.QuranAppNavGraph
import com.seifmortada.applications.quran.core.navigation.destinations.QuranScreens
import com.seifmortada.applications.quran.core.navigation.destinations.topLevelDestinations

@Composable
fun QuranApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)
    var selectedItemIndex = when (currentBackStack?.destination?.route) {
        QuranScreens.Home::class.simpleName -> 0
        QuranScreens.Reciters::class.simpleName -> 1
        QuranScreens.Settings::class.simpleName -> 2
        else -> -1
    }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    topLevelDestinations.forEachIndexed { index, topLevelDestination ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(topLevelDestination.route)
                            },
                            label = { Text(text = topLevelDestination.title) },
                            icon = {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) topLevelDestination.selectedIcon else topLevelDestination.unSelectedIcon,
                                    contentDescription = topLevelDestination.title,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                    }
                }
            }) { innerPadding ->
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
