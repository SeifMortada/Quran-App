package com.seifmortada.applications.quran.core.navigation

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
    data class ReciterTilawahChapters(val tilawahId:Int)

    @Serializable
    data class ReciterTilawahDetail(val reciterId: Int)

    @Serializable
    data class ReciterTilawahRecitation(val surahAndReciterId: Int)

}