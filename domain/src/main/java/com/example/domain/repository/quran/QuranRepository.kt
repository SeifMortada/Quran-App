package com.example.domain.repository.quran

import com.example.domain.model.SurahModel

interface QuranRepository {
    suspend fun getQuran(): List<SurahModel>

    suspend fun getSurahById(id: Int): SurahModel
}