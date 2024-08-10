package com.seifmortada.applications.quran.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.domain.model.response.quran.Ayah

class Converters {

    @TypeConverter
    fun fromAyahList(value: List<Ayah>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Ayah>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAyahList(value: String): List<Ayah> {
        val gson = Gson()
        val type = object : TypeToken<List<Ayah>>() {}.type
        return gson.fromJson(value, type)
    }
}
