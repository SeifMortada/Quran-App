package com.seifmortada.applications.quran.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.local.room.entities.quran.Verse

class VerseConverter {
    @TypeConverter
    fun fromVerseList(verses: List<Verse>): String {
        val type = object : TypeToken<List<Verse>>() {}.type
        return Gson().toJson(verses, type)
    }

    @TypeConverter
    fun toVerseList(verseString: String): List<Verse> {
        val type = object : TypeToken<List<Verse>>() {}.type
        return Gson().fromJson(verseString, type)
    }
}