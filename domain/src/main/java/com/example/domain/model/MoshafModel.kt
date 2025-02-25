package com.example.domain.model


import kotlinx.serialization.Serializable

@Serializable
data class MoshafModel(
    val id: Int,
    val moshafType: Int,
    val name: String,
    val server: String,
    val surahList: String,
    val surahTotal: Int
)
