package com.seifmortada.applications.quran.core.navigation

import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import kotlinx.serialization.Serializable

object QuranScreens {

    @Serializable
    object Home

    @Serializable
    object Azkars

    @Serializable
    data class Zikr(val zikrItemId: Int)

    @Serializable
    object QuranChapters

    @Serializable
    data class Surah(val surahId: Int)

    @Serializable
    object Reciters

    @Serializable
    data class ReciterTilawahChapters(val telawah:MoshafModel)

    @Serializable
    data class ReciterTilawahDetail(val reciter:ReciterModel)

    @Serializable
    data class ReciterTilawahRecitation(val surahAndReciter: SurahMoshafReciter)

}