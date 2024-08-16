package com.seifmortada.applications.quran.data.model.quran

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verse(
    val id: Int,
    val text: String
) : Parcelable