package com.seifmortada.applications.quran.core.navigation.destinations

import com.example.domain.model.AzkarModel
import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import kotlinx.serialization.Serializable

object QuranScreens {

    @Serializable
    object Splash

    @Serializable
    object Home

    @Serializable
    object Azkars

    @Serializable
    object AzkarsRoute

    @Serializable
    object Settings

    @Serializable
    data class Zikr(val zikrItem: AzkarModel)

    @Serializable
    object QuranChapters

    @Serializable
    object QuranChaptersRoute

    @Serializable
    data class Surah(val surahId: Int)

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

}