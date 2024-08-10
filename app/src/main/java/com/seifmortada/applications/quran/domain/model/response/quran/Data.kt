package com.seifmortada.applications.quran.domain.model.response.quran

data class Data(
    val edition: Edition,
    val surahs: List<Surah>
)