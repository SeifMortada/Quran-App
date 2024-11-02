package com.seifmortada.applications.quran.data.rest.response.reciters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecitersResponse(
    val reciters: List<Reciter>
): Parcelable