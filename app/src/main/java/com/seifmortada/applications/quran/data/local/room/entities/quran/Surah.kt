package com.seifmortada.applications.quran.data.local.room.entities.quran

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.seifmortada.applications.quran.utils.VerseConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "surah_table")
data class Surah(
    @PrimaryKey val id: Int,
    val name: String,
    val total_verses: Int,
    val transliteration: String,
    val type: String,
    @TypeConverters(VerseConverter::class)
    val verses: List<Verse>
) : Parcelable
