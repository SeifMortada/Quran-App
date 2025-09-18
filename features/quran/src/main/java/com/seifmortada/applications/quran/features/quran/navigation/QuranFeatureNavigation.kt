package com.seifmortada.applications.quran.features.quran.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.seifmortada.applications.quran.features.quran.presentation.chapters.QuranChaptersRoute
import com.seifmortada.applications.quran.features.quran.presentation.surah.SurahRoute
import kotlinx.serialization.Serializable

/**
 * Navigation routes and utilities for Quran feature module
 */
@Serializable
object QuranChapters

@Serializable
object QuranChaptersRoute

@Serializable
data class Surah(val surahId: Int)


fun NavGraphBuilder.quranSection(
    onBackClick: () -> Unit,
    onChapterClick: (Int) -> Unit
) {
    navigation<QuranChaptersRoute>(startDestination = QuranChapters) {
        composable<QuranChapters> {
            QuranChaptersRoute(
                onBackClick = onBackClick,
                onChapterClick = { surahId ->
                    onChapterClick(surahId)
                }
            )
        }
        composable<Surah> {
            val args = it.toRoute<Surah>()
            SurahRoute(
                surahId = args.surahId,
                onBackClick = onBackClick
            )
        }
    }


}
