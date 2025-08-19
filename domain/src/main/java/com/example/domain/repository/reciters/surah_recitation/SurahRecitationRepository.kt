package com.example.domain.repository.reciters.surah_recitation

import com.example.domain.model.NetworkResult
import com.example.domain.usecase.DownloadProgress
import kotlinx.coroutines.flow.Flow


interface SurahRecitationRepository {
    suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress>
}