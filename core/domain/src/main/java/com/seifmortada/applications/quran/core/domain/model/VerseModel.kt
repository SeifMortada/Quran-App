package com.seifmortada.applications.quran.core.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class VerseModel(
 val id: Int,
val text: String,
val surahId: Int
) : Parcelable
