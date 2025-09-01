package com.seifmortada.applications.quran.core.data.local.data_source

import android.content.res.AssetManager
import com.seifmortada.applications.quran.core.data.local.room.entities.azkar.AzkarItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AzkarJsonDataSource(private val assetManager: AssetManager) {

    fun getAzkarFromJson(): List<AzkarItem> {
        val json = assetManager.open("adhkar.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<AzkarItem>>() {}.type
        return Gson().fromJson(json, type)
    }
}
