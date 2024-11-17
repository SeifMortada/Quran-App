package com.example.data.local.room.entities.quran
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.local.room.converter.VerseConverter

@Entity(tableName = "surah_table")
data class SurahEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val total_verses: Int,
    val transliteration: String,
    val type: String,
    @TypeConverters(VerseConverter::class)
    var verses: List<VerseEntity>
)
