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
import com.seifmortada.applications.quran.features.quran.QuranChapters
import com.seifmortada.applications.quran.features.quran.Surah
import com.seifmortada.applications.quran.features.quran.quranSection
import com.seifmortada.applications.quran.features.reciter.ReciterTilawahChapters
import com.seifmortada.applications.quran.features.reciter.ReciterTilawahDetail
import com.seifmortada.applications.quran.features.reciter.ReciterTilawahRecitation
import com.seifmortada.applications.quran.features.reciter.Reciters
import com.seifmortada.applications.quran.features.reciter.recitersSection
import com.seifmortada.applications.quran.features.settings.settingsSection
import com.seifmortada.applications.quran.features.splash.SplashScreen
import com.seifmortada.applications.quran.features.zikr.Azkars
import com.seifmortada.applications.quran.features.zikr.Zikr
import com.seifmortada.applications.quran.features.zikr.zikrSection

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
