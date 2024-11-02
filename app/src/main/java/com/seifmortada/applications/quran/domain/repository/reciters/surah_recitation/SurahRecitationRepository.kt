package com.seifmortada.applications.quran.domain.repository.reciters.surah_recitation

import com.seifmortada.applications.quran.data.rest.utils.NetworkResult

interface SurahRecitationRepository {
    suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): NetworkResult<String>
}