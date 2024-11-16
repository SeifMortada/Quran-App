package com.example.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class SurahModel(
    val id: Int,
    val name: String,
    val totalVerses: Int,
    val transliteration: String,
    val type: String,
    val verses: List<VerseModel>
) : Parcelable
