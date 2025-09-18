package com.seifmortada.applications.quran.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seifmortada.applications.quran.core.navigation.destinations.Home
import com.seifmortada.applications.quran.core.navigation.destinations.Splash
import com.seifmortada.applications.quran.features.home.HomeRoute
import com.seifmortada.applications.quran.features.quran.navigation.QuranChapters
import com.seifmortada.applications.quran.features.quran.navigation.Surah
import com.seifmortada.applications.quran.features.quran.navigation.quranSection
import com.seifmortada.applications.quran.features.reciter.navigation.ReciterTilawahChapters
import com.seifmortada.applications.quran.features.reciter.navigation.ReciterTilawahDetail
import com.seifmortada.applications.quran.features.reciter.navigation.ReciterTilawahRecitation
import com.seifmortada.applications.quran.features.reciter.navigation.Reciters
import com.seifmortada.applications.quran.features.reciter.navigation.recitersSection
import com.seifmortada.applications.quran.features.settings.navigation.settingsSection
import com.seifmortada.applications.quran.features.splash.SplashScreen
import com.seifmortada.applications.quran.features.zikr.navigation.Azkars
import com.seifmortada.applications.quran.features.zikr.navigation.Zikr
import com.seifmortada.applications.quran.features.zikr.navigation.zikrSection

@Composable
fun QuranAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Splash,
        modifier = modifier
    ) {
        composable<Splash> {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Home) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeRoute(
                onZikrClick = { navController.navigate(Azkars){
                    popUpTo(Home) { inclusive = false }
                    launchSingleTop = true
                    restoreState = true
                } },
                onQuranClick = { navController.navigate(QuranChapters){
                    popUpTo(Home) { inclusive = false }
                    launchSingleTop = true
                    restoreState = true
                } },
                onReciterClick = { navController.navigate(Reciters){
                    popUpTo(Home) { inclusive = false }
                    launchSingleTop = true
                    restoreState = true
                } }
            )
        }

        settingsSection()

        quranSection(
            onBackClick = { navController.navigateUp() },
            onChapterClick = { surahId ->
                navController.navigate(Surah(surahId))
            }
        )
        zikrSection(
            onBackClick = { navController.navigateUp() },
            onZikrClicked = { zikr ->
                navController.navigate(Zikr(zikr))
            }
        )
        recitersSection(
            onBackClick = { navController.navigateUp() },
            onReciterClick = { reciter ->
                navController.navigate(ReciterTilawahDetail(reciter))
            },
            onTelawahClick = { tilawah ->
                navController.navigate(ReciterTilawahChapters(tilawah))
            },
            onSurahClicked = { surahAndTelawah ->
                navController.navigate(ReciterTilawahRecitation(surahAndTelawah))
            }
        )

    }
}
