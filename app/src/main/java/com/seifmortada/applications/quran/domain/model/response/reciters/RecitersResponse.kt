package com.seifmortada.applications.quran.domain.model.response.reciters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecitersResponse(
    val reciters: List<Reciter>
): Parcelable