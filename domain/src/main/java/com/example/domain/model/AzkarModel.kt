package com.example.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AzkarModel(
    val array: List<AzkarItemModel>,
    val audio: String,
    val category: String,
    val filename: String,
    val id: Int
): Parcelable

@Parcelize
data class AzkarItemModel(
    val audio: String,
    val count: Int,
    val filename: String,
    val id: Int,
    val text: String
): Parcelable
