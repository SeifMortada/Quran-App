package com.example.domain.model

data class AzkarModel(
    val array: List<AzkarItemModel>,
    val audio: String,
    val category: String,
    val filename: String,
    val id: Int
)

data class AzkarItemModel(
    val audio: String,
    val count: Int,
    val filename: String,
    val id: Int,
    val text: String
)
