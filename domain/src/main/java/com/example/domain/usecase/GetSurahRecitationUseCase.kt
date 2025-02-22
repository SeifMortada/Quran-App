package com.example.domain.usecase

import android.util.Log
import com.example.domain.model.NetworkResult
import com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class GetSurahRecitationUseCase(private val surahRecitationRepository: SurahRecitationRepository) {
    suspend operator fun invoke(
        server: String,
        surahNumber: String
    ): NetworkResult<Pair<String, Long>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = surahRecitationRepository.getSurahRecitation(server, surahNumber)
                when (result) {
                    is NetworkResult.Error -> NetworkResult.Error(result.errorMessage)
                    is NetworkResult.Success -> handleSuccessUrlFound(result.data)
                    else -> NetworkResult.Loading
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message.toString())
            }
        }
    }

    private fun handleSuccessUrlFound(url: String): NetworkResult<Pair<String, Long>> {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connect()
        val fileSize =
            connection.contentLengthLong // we do this step to get the audio size to show it in the slider
        connection.disconnect()
        return if (fileSize > 0) {
            NetworkResult.Success(Pair(url, fileSize))
        } else NetworkResult.Error("خطأ")
    }

}
