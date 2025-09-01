package com.seifmortada.applications.quran.core.data.local.room.entities.azkar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zikr_table")
data class AzkarItemData(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int,
    val audio: String,
    val count: Int,
    val filename: String,
    val id: Int,
    val text: String
)
