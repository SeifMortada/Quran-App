package com.example.data.repository.reciters.surah_recitation

import com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import java.util.Locale

class SurahRecitationRepositoryImpl(private val recitersApiService: RecitersApi) :
    SurahRecitationRepository {
    override suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): NetworkResult<String> {

        val formattedSurahNumber = String.format(Locale.US, "%03d", surahNumber.toInt()) // "001"
        val url = "$server$formattedSurahNumber.mp3"
        return try {
            val response = recitersApiService.getSurahRecitation(url)
            if (response.isSuccessful && response.body() != null) NetworkResult.Success(url)
            else NetworkResult.Error("خطأ")
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }
}