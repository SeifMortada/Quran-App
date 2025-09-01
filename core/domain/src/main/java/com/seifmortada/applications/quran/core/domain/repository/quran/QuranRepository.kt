package com.seifmortada.applications.quran.core.domain.repository.quran

import com.seifmortada.applications.quran.core.domain.model.SurahModel

interface QuranRepository {
    suspend fun getQuran(): List<SurahModel>

    suspend fun getSurahById(id: Int): SurahModel
}
