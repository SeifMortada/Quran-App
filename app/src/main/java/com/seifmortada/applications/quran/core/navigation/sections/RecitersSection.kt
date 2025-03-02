package com.seifmortada.applications.quran.core.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.core.navigation.destinations.QuranScreens
import com.seifmortada.applications.quran.core.navigation.types.CustomNavType
import com.seifmortada.applications.quran.features.reciter_tilawah_chapters.ReciterAllSurahsRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_detail.ReciterTelawahDetailsRoute
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.ReciterSurahRecitationRoute
import com.seifmortada.applications.quran.features.reciters.ReciterRoute
import kotlin.reflect.typeOf


fun NavGraphBuilder.recitersSection(
    onBackClick: () -> Unit,
    onReciterClick: (ReciterModel) -> Unit,
    onTelawahClick: (MoshafModel) -> Unit,
    onSurahClicked: (SurahMoshafReciter) -> Unit
) {
    navigation<QuranScreens.RecitersRoute>(startDestination = QuranScreens.Reciters) {
        composable<QuranScreens.Reciters> {
            ReciterRoute(
                onBackClick = onBackClick,
                onReciterClick = { reciter ->
                    onReciterClick(reciter)
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
                onBackClick = onBackClick,
                onTelawahClick = { tilawah ->
                    onTelawahClick(tilawah)
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
                onBackClicked = onBackClick,
                availableSurahsWithThisTelawah = args.telawah,
                onSurahClicked = { surahAndTelawah ->
                    onSurahClicked(surahAndTelawah)
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
                onBackClicked = onBackClick
            )
        }
    }

}