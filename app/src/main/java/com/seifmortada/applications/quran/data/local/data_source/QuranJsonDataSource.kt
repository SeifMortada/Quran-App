package com.seifmortada.applications.quran.data.local.data_source

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah

class QuranJsonDataSource(private val context: Context) {

    fun getQuranFromJson(): List<Surah> {
        val json = context.assets.open("quran.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Surah>>() {}.type
        return Gson().fromJson(json, type)
    }
}