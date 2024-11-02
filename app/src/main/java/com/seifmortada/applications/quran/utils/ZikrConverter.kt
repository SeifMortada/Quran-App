package com.seifmortada.applications.quran.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItemData
import com.seifmortada.applications.quran.data.local.room.entities.quran.Verse

class ZikrConverter {
    @TypeConverter
    fun fromZikrList(zikr: List<AzkarItemData>): String {
        val type = object : TypeToken<List<AzkarItemData>>() {}.type
        return Gson().toJson(zikr, type)
    }

    @TypeConverter
    fun toZikrList(zikrString: String): List<AzkarItemData> {
        val type = object : TypeToken<List<AzkarItemData>>() {}.type
        return Gson().fromJson(zikrString, type)
    }
}