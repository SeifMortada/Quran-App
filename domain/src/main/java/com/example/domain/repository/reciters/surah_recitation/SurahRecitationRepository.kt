package com.example.domain.repository.reciters.surah_recitation

import com.example.domain.model.NetworkResult


interface SurahRecitationRepository {
    suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): NetworkResult<String>
}