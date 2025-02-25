package com.seifmortada.applications.quran.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seifmortada.applications.quran.features.azkar_detail.composables.ZikrScreen
import com.seifmortada.applications.quran.features.home.HomeRoute
import com.seifmortada.applications.quran.features.quran_chapters_feature.QuranChaptersRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_chapters.ReciterAllSurahsRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_detail.ReciterTelawahDetailsRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.ReciterSurahRecitationRoute
import com.seifmortada.applications.quran.features.reciters.ReciterRoute
import com.seifmortada.applications.quran.features.surah_feature.SurahRoute

@Composable
fun QuranAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()

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
        composable<QuranScreens.QuranChapters> {
            QuranChaptersRoute(
                onBackClick = { navController.navigateUp() },
                onChapterClick = { surahId ->
                    navController.navigate(QuranScreens.Surah(surahId = surahId))
                }
            )
        }
        composable<QuranScreens.Surah> {
            val args = it.toRoute<QuranScreens.Surah>()
            SurahRoute(
                surahId = args.surahId,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<QuranScreens.Azkars> {}
        composable<QuranScreens.Zikr> {
            val args = it.toRoute<QuranScreens.Zikr>()
            ZikrScreen(
                zikrId = args.zikrItemId,
                onBackButtonClicked = { navController.navigateUp() }
            )
        }
        composable<QuranScreens.Reciters> {
            ReciterRoute(
                onBackClick = { navController.navigateUp() },
                onReciterClick = { reciter ->
                    navController.navigate(QuranScreens.ReciterTilawahDetail(reciter))
                }
            )
        }
        composable<QuranScreens.ReciterTilawahDetail> {
            val args = it.toRoute<QuranScreens.ReciterTilawahDetail>()
            ReciterTelawahDetailsRoute(
                reciterId = args.reciterId,
                onBackClick = { navController.navigateUp() },
                onTelawahClick = { tilawah ->
                    navController.navigate(QuranScreens.ReciterTilawahChapters(tilawah))
                }
            )
        }
        composable<QuranScreens.ReciterTilawahChapters> {
            val args = it.toRoute<QuranScreens.ReciterTilawahChapters>()
            ReciterAllSurahsRoute(
                onBackClicked = { navController.navigateUp() },
                availableSurahsWithThisTelawahId = args.tilawahId,
                onSurahClicked = { surahAndTelawah ->
                    navController.navigate(QuranScreens.ReciterTilawahRecitation(surahAndTelawah))
                }
            )
        }
        composable<QuranScreens.ReciterTilawahRecitation> {
            val args = it.toRoute<QuranScreens.ReciterTilawahRecitation>()
            ReciterSurahRecitationRoute(
                surahAndServer = args.surahAndReciterId,
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}