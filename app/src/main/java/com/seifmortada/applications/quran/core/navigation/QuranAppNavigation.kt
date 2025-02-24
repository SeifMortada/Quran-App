package com.seifmortada.applications.quran.core.navigation

import androidx.navigation.NavHostController
import com.example.domain.model.AzkarModel
import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter

private object QuranScreens {
    const val HOME_SCREEN = "homeScreen"
    const val AZKARS_SCREEN = "azkarsScreen"
    const val AZKAR_DETAIL_SCREEN = "azkarDetailScreen"
    const val QURAN_CHAPTERS_SCREEN = "quranChaptersScreen"
    const val SURAH_SCREEN = "surahScreen"
    const val RECITERS_SCREEN = "recitersScreen"
    const val RECITER_TILAWAH_SCREEN = "reciterTelawahScreen"
    const val RECITER_TILAWAH_DETAIL_SCREEN = "reciterTelawahDetailScreen"
    const val RECITER_TILAWAH_RECITATION_SCREEN = "reciterTelawahRecitationScreen"
}

object QuranDestinationsArgs {
    const val ZIKR_DETAIL_ARGS = "zikrItem"
    const val SURAH_DETAIL_ARGS = "surahItem"
    const val RECITER_TILAWAHS_ARGS = "reciterTelawah"
    const val RECITER_TILAWAH_DETAIL_ARGS = "reciterTelawahDetail"
    const val RECITER_TILAWAH_RECITATION_ARGS = "reciterTelawahRecitation"
}

object QuranDestinations {
    const val HOME_ROUTE = QuranScreens.HOME_SCREEN
    const val AZKARS_ROUTE = QuranScreens.AZKARS_SCREEN
    const val AZKAR_DETAIL_ROUTE =
        "${QuranScreens.AZKAR_DETAIL_SCREEN}?${QuranDestinationsArgs.ZIKR_DETAIL_ARGS}={${QuranDestinationsArgs.ZIKR_DETAIL_ARGS}}"
    const val QURAN_CHAPTERS_ROUTE = QuranScreens.QURAN_CHAPTERS_SCREEN
    const val SURAH_ROUTE =
        "${QuranScreens.SURAH_SCREEN}?${QuranDestinationsArgs.SURAH_DETAIL_ARGS}={${QuranDestinationsArgs.SURAH_DETAIL_ARGS}}"
    const val RECITERS_ROUTE = QuranScreens.RECITERS_SCREEN
    const val RECITER_TILAWAH_ROUTE =
        "${QuranScreens.RECITER_TILAWAH_SCREEN}?${QuranDestinationsArgs.RECITER_TILAWAHS_ARGS}={${QuranDestinationsArgs.RECITER_TILAWAHS_ARGS}}"
    const val RECITER_TILAWAH_DETAIL_ROUTE =
        "${QuranScreens.RECITER_TILAWAH_DETAIL_SCREEN}?${QuranDestinationsArgs.RECITER_TILAWAH_DETAIL_ARGS}={${QuranDestinationsArgs.RECITER_TILAWAH_DETAIL_ARGS}}"
    const val RECITER_TILAWAH_RECITATION_ROUTE =
        "${QuranScreens.RECITER_TILAWAH_RECITATION_SCREEN}?${QuranDestinationsArgs.RECITER_TILAWAH_RECITATION_ARGS}={${QuranDestinationsArgs.RECITER_TILAWAH_RECITATION_ARGS}}"
}

class QuranNavigationActions(private val navController: NavHostController) {
    fun navigateToHome() {
        navController.navigate(QuranDestinations.HOME_ROUTE) {
            popUpTo(QuranDestinations.HOME_ROUTE) { inclusive = true }
        }
    }

    fun navigateToAzkars() {
        navController.navigate(QuranDestinations.AZKARS_ROUTE)
    }

    fun navigateToAzkarDetails(zikrItem: AzkarModel) {
        navController.navigate("${QuranScreens.AZKAR_DETAIL_SCREEN}?${QuranDestinationsArgs.ZIKR_DETAIL_ARGS}=${zikrItem}")
    }

    fun navigateToQuranChapters() {
        navController.navigate(QuranDestinations.QURAN_CHAPTERS_ROUTE)
    }

    fun navigateToReciters() {
        navController.navigate(QuranDestinations.RECITERS_ROUTE)
    }

    fun navigateToSurah(surahId: Int) {
        if (surahId != -1)
            navController.navigate("${QuranScreens.SURAH_SCREEN}?${QuranDestinationsArgs.SURAH_DETAIL_ARGS}=${surahId}")
    }

    fun navigateToReciterTelawah(telawah: MoshafModel) {
        navController.navigate("${QuranScreens.RECITER_TILAWAH_SCREEN}?${QuranDestinationsArgs.RECITER_TILAWAHS_ARGS}=${telawah}")
    }
    fun navigateToReciterTelawahDetail(telawahDetails: ReciterModel) {
        navController.navigate("${QuranScreens.RECITER_TILAWAH_DETAIL_SCREEN}?${QuranDestinationsArgs.RECITER_TILAWAH_DETAIL_ARGS}=${telawahDetails}")
    }
    fun navigateToReciterTelawahRecitation(surahTelawahReciter:SurahMoshafReciter) {
        navController.navigate("${QuranScreens.RECITER_TILAWAH_RECITATION_SCREEN}?${QuranDestinationsArgs.RECITER_TILAWAH_RECITATION_ARGS}=${surahTelawahReciter}")

    }
}