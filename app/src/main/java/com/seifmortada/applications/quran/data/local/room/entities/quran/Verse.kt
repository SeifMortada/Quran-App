package com.seifmortada.applications.quran.data.local.room.entities.quran

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "verse_table")
data class Verse(
    @PrimaryKey val id: Int,
    val text: String,
    val surahId: Int // Foreign key to associate with Surah
) : Parcelable
