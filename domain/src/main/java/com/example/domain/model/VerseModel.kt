package com.example.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class VerseModel(
 val id: Int,
val text: String,
val surahId: Int
) : Parcelable
