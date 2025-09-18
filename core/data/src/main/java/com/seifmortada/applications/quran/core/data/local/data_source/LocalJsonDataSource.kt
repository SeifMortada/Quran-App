package com.seifmortada.applications.quran.core.data.local.data_source

import com.seifmortada.applications.quran.core.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.core.data.local.room.entities.quran.SurahEntity

interface LocalJsonDataSource {

    fun getQuranData() : List<SurahEntity>

    fun getAzkarData(): List<AzkarItem>
}