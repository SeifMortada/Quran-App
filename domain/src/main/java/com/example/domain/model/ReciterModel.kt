package com.example.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class ReciterModel(
    val date: String,
    val id: Int,
    val letter: String,
    val moshaf: List<MoshafModel>,
    val name: String
): Parcelable