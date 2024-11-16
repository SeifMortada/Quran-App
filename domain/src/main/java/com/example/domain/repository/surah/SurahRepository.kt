package com.example.domain.repository.surah


interface SurahRepository {
    suspend fun getAyahRecitation(ayahNumber: String): NetworkResult<String>
}