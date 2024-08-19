package com.seifmortada.applications.quran.data.repository.reciters.surah_recitation

import com.seifmortada.applications.quran.data.remote.service.RecitersApiService
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.domain.model.response.reciters.RecitersResponse

class SurahRecitationRepositoryImpl(private val recitersApiService: RecitersApiService) :
    SurahRecitationRepository {
    override suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): NetworkResult<String> {

        val formattedSurahNumber = String.format("%03d", surahNumber.toInt()) // "001"
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