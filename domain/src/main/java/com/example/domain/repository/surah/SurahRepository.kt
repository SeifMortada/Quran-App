package com.example.domain.repository.surah

import com.example.domain.model.NetworkResult


interface SurahRepository {
    suspend fun getAyahRecitation(ayahNumber: String): NetworkResult<String>
}