package com.seifmortada.applications.quran.domain.model.response.quran

data class Edition(
    val englishName: String,
    val format: String,
    val identifier: String,
    val language: String,
    val name: String,
    val type: String
)