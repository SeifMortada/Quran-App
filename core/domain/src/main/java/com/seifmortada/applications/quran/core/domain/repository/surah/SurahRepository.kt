package com.seifmortada.applications.quran.core.domain.repository.surah

import com.seifmortada.applications.quran.core.domain.model.NetworkResult


interface SurahRepository {
    suspend fun getAyahRecitation(ayahNumber: String): NetworkResult<String>
}
