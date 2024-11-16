package com.example.data.local.room.converter

import androidx.room.TypeConverter
import com.example.data.local.room.entities.quran.VerseEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VerseConverter {
    @TypeConverter
    fun fromVerseList(vers: List<VerseEntity>): String {
        val type = object : TypeToken<List<VerseEntity>>() {}.type
        return Gson().toJson(vers, type)
    }

    @TypeConverter
    fun toVerseList(verseString: String): List<VerseEntity> {
        val type = object : TypeToken<List<VerseEntity>>() {}.type
        return Gson().fromJson(verseString, type)
    }
}