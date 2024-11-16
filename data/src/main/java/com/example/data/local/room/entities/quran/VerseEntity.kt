package com.example.data.local.room.entities.quran
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verse_table")
data class VerseEntity(
    @PrimaryKey val id: Int,
    val text: String,
    val surahId: Int // Foreign key to associate with Surah
)
