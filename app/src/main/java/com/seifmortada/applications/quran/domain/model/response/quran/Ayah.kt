package com.seifmortada.applications.quran.domain.model.response.quran

data class Ayah(
    val hizbQuarter: Int,
    val juz: Int,
    val manzil: Int,
    val number: Int,
    val numberInSurah: Int,
    val page: Int,
    val ruku: Int,
    val sajda: Any,
    val text: String
)
data class SajdaObject(
    val id: Int,
    val recommended: Boolean,
    val obligatory: Boolean
)
