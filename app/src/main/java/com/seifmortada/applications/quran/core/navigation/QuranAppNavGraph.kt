package com.seifmortada.applications.quran.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seifmortada.applications.quran.core.navigation.destinations.QuranScreens
import com.seifmortada.applications.quran.core.navigation.sections.quranSection
import com.seifmortada.applications.quran.core.navigation.sections.recitersSection
import com.seifmortada.applications.quran.core.navigation.sections.zikrSection
import com.seifmortada.applications.quran.features.home.HomeRoute
import com.seifmortada.applications.quran.features.settings.SettingsRoute

@Composable
fun QuranAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = QuranScreens.Home,
        modifier = modifier
    ) {
        composable<QuranScreens.Home> {
            HomeRoute(
                onZikrClick = { navController.navigate(QuranScreens.Azkars) },
                onQuranClick = { navController.navigate(QuranScreens.QuranChapters) },
                onReciterClick = { navController.navigate(QuranScreens.Reciters) }
            )
        }
        composable<QuranScreens.Settings> {
            SettingsRoute()
        }
        quranSection(
            onBackClick = { navController.navigateUp() },
            onChapterClick = { surahId ->
                navController.navigate(QuranScreens.Surah(surahId))
            }
        )
        zikrSection(
            onBackClick = { navController.navigateUp() },
            onZikrClicked = { zikr ->
                navController.navigate(QuranScreens.Zikr(zikr))
            }
        )
        recitersSection(
            onBackClick = { navController.navigateUp() },
            onReciterClick = { reciter ->
                navController.navigate(QuranScreens.ReciterTilawahDetail(reciter))
            },
            onTelawahClick = { tilawah ->
                navController.navigate(QuranScreens.ReciterTilawahChapters(tilawah))
            },
            onSurahClicked = { surahAndTelawah ->
                navController.navigate(QuranScreens.ReciterTilawahRecitation(surahAndTelawah))
            }
        )

    }
}