package com.seifmortada.applications.quran.core.data.local.data_source
import android.content.res.AssetManager
import com.seifmortada.applications.quran.core.data.local.room.entities.quran.SurahEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

class QuranJsonDataSource(private val assetManager: AssetManager) {

        fun getQuranFromJson(): List<SurahEntity> {
            val json = assetManager.open("quran.json").bufferedReader().use { it.readText() }

            // Create a custom Gson instance with error handling for null/empty fields
            val gson = Gson()

            // Convert the JSON into a List of SurahEntity objects
            val type = object : TypeToken<List<SurahEntity>>() {}.type
            val surahs: List<SurahEntity> = gson.fromJson(json, type)

            // Ensure `vers` is never null in each SurahEntity
            surahs.forEach { surah ->
                Timber.d("SURAHS ARE $surahs")
                if (surah.verses == null) {
                    surah.verses = emptyList() // Default to empty list if `vers` is null
                }
            }

            return surahs
    }

}
