package com.example.domain.usecase

import android.content.Context
import android.util.Log
import com.example.domain.model.NetworkResult
import com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class GetSurahRecitationUseCase(
    private val surahRecitationRepository: SurahRecitationRepository
) {
    suspend operator fun invoke(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress> {
        return surahRecitationRepository.getSurahRecitation(server, surahNumber)
    }
}
data class DownloadProgress(
    val downloadedBytes: Long,
    val totalBytes: Long,
    val progress: Float ,
    val localPath: String? = null
)
