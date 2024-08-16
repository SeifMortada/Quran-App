package com.seifmortada.applications.quran.domain.model.response.reciters

data class Reciter(
    val date: String,
    val id: Int,
    val letter: String,
    val moshaf: List<Moshaf>,
    val name: String
)