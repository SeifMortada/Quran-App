package com.seifmortada.applications.quran.features.zikr

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.ui.navigation.types.CustomNavType
import com.seifmortada.applications.quran.features.zikr.azkars.AzkarsRoute
import com.seifmortada.applications.quran.features.zikr.zikr.ZikrRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

/**
 * Navigation routes and utilities for Zikr feature module
 */

@Serializable
object AzkarsRoute

@Serializable
object Azkars

@Serializable
data class Zikr(val zikrItem: AzkarModel)


fun NavGraphBuilder.zikrSection(
    onBackClick: () -> Unit,
    onZikrClicked: (AzkarModel) -> Unit
) {
    navigation<AzkarsRoute>(startDestination = Azkars) {
        composable<Azkars> {
            AzkarsRoute(
                onBackClick = onBackClick,
                onZikrClicked = { zikr ->
                    onZikrClicked(zikr)
                }
            )
        }
        composable<Zikr>(
            typeMap = mapOf(
                typeOf<AzkarModel>() to CustomNavType.azkarModel
            )
        ) {
            val args = it.toRoute<Zikr>()
            ZikrRoute(
                zikr = args.zikrItem,
                onBackClicked = onBackClick
            )
        }
    }
}
