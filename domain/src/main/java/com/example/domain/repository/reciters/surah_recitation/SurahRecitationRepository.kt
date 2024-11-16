package com.example.domain.repository.reciters.surah_recitation


interface SurahRecitationRepository {
    suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): NetworkResult<String>
}