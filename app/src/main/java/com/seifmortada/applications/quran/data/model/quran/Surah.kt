package com.seifmortada.applications.quran.data.model.quran

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Surah(
    val id: Int,
    val name: String,
    val total_verses: Int,
    val transliteration: String,
    val type: String,
    val verses: List<Verse>
):Parcelable