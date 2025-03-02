package com.seifmortada.applications.quran.core.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.seifmortada.applications.quran.core.navigation.destinations.QuranScreens
import com.seifmortada.applications.quran.features.quran_chapters.QuranChaptersRoute
import com.seifmortada.applications.quran.features.surah.SurahRoute

fun NavGraphBuilder.quranSection(
    onBackClick: () -> Unit,
    onChapterClick: (Int) -> Unit
) {
    navigation<QuranScreens.QuranChaptersRoute>(startDestination = QuranScreens.QuranChapters) {
        composable<QuranScreens.QuranChapters> {
            QuranChaptersRoute(
                onBackClick = onBackClick,
                onChapterClick = { surahId ->
                    onChapterClick(surahId)
                }
            )
        }
        composable<QuranScreens.Surah> {
            val args = it.toRoute<QuranScreens.Surah>()
            SurahRoute(
                surahId = args.surahId,
                onBackClick = onBackClick
            )
        }
    }


}