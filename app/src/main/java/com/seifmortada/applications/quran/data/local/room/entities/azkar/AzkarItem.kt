package com.seifmortada.applications.quran.data.local.room.entities.azkar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "azkar_table")
data class AzkarItem(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int,
    val array: List<AzkarItemData>,
    val audio: String,
    val category: String,
    val filename: String,
    val id: Int
)