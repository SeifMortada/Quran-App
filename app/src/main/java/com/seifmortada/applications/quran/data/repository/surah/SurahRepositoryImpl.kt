package com.seifmortada.applications.quran.data.repository.surah

import com.seifmortada.applications.quran.data.remote.service.RecitersApiService
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import java.io.InputStream

class SurahRepositoryImpl(private val recitersApiService: RecitersApiService) : SurahRepository {
    override suspend fun getAyahRecitation(
        ayahNumber: String
    ): NetworkResult<String> {
        val url = "https://cdn.islamic.network/quran/audio/64/ar.husary/$ayahNumber.mp3"
        return try {
            val response = recitersApiService.getAyahRecitation(url)
            NetworkResult.Success(url)
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }
}
