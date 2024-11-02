package com.seifmortada.applications.quran.domain.repository.quran

import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah

interface QuranRepository {
    suspend fun getQuran(): List<Surah>
}