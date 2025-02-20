package com.example.data.rest.response.reciters

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Moshaf(
    val id: Int,
    @SerializedName("moshaf_type")
    val moshafType: Int,
    val name: String,
    val server: String,
    @SerializedName("surah_list")
    val surahList: String,
    @SerializedName("surah_total")
    val surahTotal: Int
)