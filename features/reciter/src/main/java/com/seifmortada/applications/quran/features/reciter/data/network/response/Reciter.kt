package com.seifmortada.applications.quran.features.reciter.data.network.response

data class Reciter(
    val date: String,
    val id: Int,
    val letter: String,
    val moshaf: List<Moshaf>,
    val name: String
)
