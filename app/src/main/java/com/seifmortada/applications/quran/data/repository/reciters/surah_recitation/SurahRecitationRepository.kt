package com.seifmortada.applications.quran.data.repository.reciters.surah_recitation

import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.domain.model.response.reciters.RecitersResponse

interface SurahRecitationRepository {
    suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): NetworkResult<String>
}