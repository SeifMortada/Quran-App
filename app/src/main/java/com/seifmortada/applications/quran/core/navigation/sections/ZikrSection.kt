package com.seifmortada.applications.quran.core.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.navigation.destinations.QuranScreens
import com.seifmortada.applications.quran.core.navigation.types.CustomNavType
import com.seifmortada.applications.quran.features.azkars.AzkarsRoute
import com.seifmortada.applications.quran.features.zikr.ZikrRoute
import kotlin.reflect.typeOf

fun NavGraphBuilder.zikrSection(
    onBackClick: () -> Unit,
    onZikrClicked: (AzkarModel) -> Unit
) {
    navigation<QuranScreens.AzkarsRoute>(startDestination = QuranScreens.Azkars) {
        composable<QuranScreens.Azkars> {
            AzkarsRoute(
                onBackClick = onBackClick,
                onZikrClicked = { zikr ->
                    onZikrClicked(zikr)
                }
            )
        }
        composable<QuranScreens.Zikr>(
            typeMap = mapOf(
                typeOf<AzkarModel>() to CustomNavType.azkarModel
            )
        ) {
            val args = it.toRoute<QuranScreens.Zikr>()
            ZikrRoute(
                zikr = args.zikrItem,
                onBackClicked = onBackClick
            )
        }
    }
}