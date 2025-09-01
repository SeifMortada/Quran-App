package com.seifmortada.applications.quran.core.data.local.room.converter

import androidx.room.TypeConverter
import com.seifmortada.applications.quran.core.data.local.room.entities.quran.VerseEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VerseConverter {
    @TypeConverter
    fun fromVerseList(verses: List<VerseEntity>): String {
        val type = object : TypeToken<List<VerseEntity>>() {}.type
        return Gson().toJson(verses, type)
    }

    @TypeConverter
    fun toVerseList(verseString: String): List<VerseEntity> {
        val type = object : TypeToken<List<VerseEntity>>() {}.type
        return Gson().fromJson(verseString, type)
    }
}
