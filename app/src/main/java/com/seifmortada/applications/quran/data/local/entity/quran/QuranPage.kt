package com.seifmortada.applications.quran.data.local.entity.quran

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seifmortada.applications.quran.domain.model.response.quran.Ayah
@Entity(tableName = "quran_table")
data class QuranPage(
    @PrimaryKey(autoGenerate = true)
    val id: Int ?=null,
    val pageNumber: Int,
    val hizbInfo: String,
    val ayahs: List<Ayah>
)
