package com.seifmortada.applications.quran.domain.model.response.reciters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reciter(
    val date: String,
    val id: Int,
    val letter: String,
    val moshaf: List<Moshaf>,
    val name: String
): Parcelable