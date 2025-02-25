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
import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.features.azkar_detail.composables.ZikrScreen
import com.seifmortada.applications.quran.features.home.HomeRoute
import com.seifmortada.applications.quran.features.quran_chapters_feature.QuranChaptersRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_chapters.ReciterAllSurahsRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_detail.ReciterTelawahDetailsRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.ReciterSurahRecitationRoute
import com.seifmortada.applications.quran.features.reciters.ReciterRoute
import com.seifmortada.applications.quran.features.surah_feature.SurahRoute
import kotlin.reflect.typeOf

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
        composable<QuranScreens.ReciterTilawahDetail>(
            typeMap = mapOf(
                typeOf<ReciterModel>() to CustomNavType.reciterType
            )
        ) {
            val args = it.toRoute<QuranScreens.ReciterTilawahDetail>()
            ReciterTelawahDetailsRoute(
                reciter = args.reciter,
                onBackClick = { navController.navigateUp() },
                onTelawahClick = { tilawah ->
                    navController.navigate(QuranScreens.ReciterTilawahChapters(tilawah))
                }
            )
        }
        composable<QuranScreens.ReciterTilawahChapters>(
            typeMap = mapOf(
                typeOf<MoshafModel>() to CustomNavType.tilawahType
            )
        ) {
            val args = it.toRoute<QuranScreens.ReciterTilawahChapters>()
            ReciterAllSurahsRoute(
                onBackClicked = { navController.navigateUp() },
                availableSurahsWithThisTelawah = args.telawah,
                onSurahClicked = { surahAndTelawah ->
                    navController.navigate(QuranScreens.ReciterTilawahRecitation(surahAndTelawah))
                }
            )
        }
        composable<QuranScreens.ReciterTilawahRecitation>(
            typeMap = mapOf(
                typeOf<SurahMoshafReciter>() to CustomNavType.surahTelawahReciter
            )
        ) {
            val args = it.toRoute<QuranScreens.ReciterTilawahRecitation>()
            ReciterSurahRecitationRoute(
                surahId = args.surahAndReciter.surahId,
                server = args.surahAndReciter.moshaf.server,
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}