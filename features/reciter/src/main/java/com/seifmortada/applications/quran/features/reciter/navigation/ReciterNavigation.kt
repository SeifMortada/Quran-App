package com.seifmortada.applications.quran.features.reciter.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.seifmortada.applications.quran.core.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.core.domain.model.MoshafModel
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.core.ui.navigation.types.CustomNavType
import com.seifmortada.applications.quran.features.reciter.presentation.chapters.ReciterAllSurahsRoute
import com.seifmortada.applications.quran.features.reciter.presentation.details.ReciterTelawahDetailsRoute
import com.seifmortada.applications.quran.features.reciter.presentation.recitation.ReciterSurahRecitationRoute
import com.seifmortada.applications.quran.features.reciter.presentation.reciters.ReciterRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

/**
 * Navigation routes and utilities for Reciter feature module
 */
@Serializable
object Reciters

@Serializable
object RecitersRoute

@Serializable
data class ReciterTilawahDetail(val reciter: ReciterModel)

@Serializable
data class ReciterTilawahChapters(val telawah: MoshafModel)

@Serializable
data class ReciterTilawahRecitation(val surahAndReciter: SurahMoshafReciter)



fun NavGraphBuilder.recitersSection(
    onBackClick: () -> Unit,
    onReciterClick: (ReciterModel) -> Unit,
    onTelawahClick: (MoshafModel) -> Unit,
    onSurahClicked: (SurahMoshafReciter) -> Unit
) {
    navigation<RecitersRoute>(startDestination = Reciters) {
        composable<Reciters> {
            ReciterRoute(
                onBackClick = onBackClick,
                onReciterClick = { reciter ->
                    onReciterClick(reciter)
                }
            )
        }
        composable<ReciterTilawahDetail>(
            typeMap = mapOf(
                typeOf<ReciterModel>() to CustomNavType.reciterType
            )
        ) {
            val args = it.toRoute<ReciterTilawahDetail>()
            ReciterTelawahDetailsRoute(
                reciter = args.reciter,
                onBackClick = onBackClick,
                onTelawahClick = { tilawah ->
                    onTelawahClick(tilawah)
                }
            )
        }
        composable<ReciterTilawahChapters>(
            typeMap = mapOf(
                typeOf<MoshafModel>() to CustomNavType.tilawahType
            )
        ) {
            val args = it.toRoute<ReciterTilawahChapters>()
            ReciterAllSurahsRoute(
                onBackClicked = onBackClick,
                availableSurahsWithThisTelawah = args.telawah,
                onSurahClicked = { surahAndTelawah ->
                    onSurahClicked(surahAndTelawah)
                }
            )
        }
        composable<ReciterTilawahRecitation>(
            typeMap = mapOf(
                typeOf<SurahMoshafReciter>() to CustomNavType.surahTelawahReciter
            )
        ) {
            val args = it.toRoute<ReciterTilawahRecitation>()
            ReciterSurahRecitationRoute(
                surahId = args.surahAndReciter.surahId,
                server = args.surahAndReciter.moshaf.server,
                reciterName = args.surahAndReciter.moshaf.name,
                onBackClicked = onBackClick
            )
        }
    }

}

