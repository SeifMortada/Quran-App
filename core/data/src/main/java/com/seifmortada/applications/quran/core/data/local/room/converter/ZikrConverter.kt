package com.seifmortada.applications.quran.core.data.local.room.converter

import androidx.room.TypeConverter
import com.seifmortada.applications.quran.core.data.local.room.entities.azkar.AzkarItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
