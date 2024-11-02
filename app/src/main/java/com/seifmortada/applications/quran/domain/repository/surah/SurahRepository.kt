package com.seifmortada.applications.quran.domain.repository.surah

import com.seifmortada.applications.quran.data.rest.utils.NetworkResult

interface SurahRepository {
    suspend fun getAyahRecitation(ayahNumber: String): NetworkResult<String>
}