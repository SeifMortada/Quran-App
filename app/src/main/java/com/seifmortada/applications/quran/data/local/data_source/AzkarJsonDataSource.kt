package com.seifmortada.applications.quran.data.local.data_source

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah

class AzkarJsonDataSource(private val context: Context) {

    fun getAzkarFromJson(): List<AzkarItem> {
        val json = context.assets.open("adhkar.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<AzkarItem>>() {}.type
        return Gson().fromJson(json, type)
    }
}