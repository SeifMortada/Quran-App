package com.seifmortada.applications.quran.data.repository.surah

import com.seifmortada.applications.quran.data.rest.apis.RecitersApi
import com.seifmortada.applications.quran.data.rest.utils.ApiConstant
import com.seifmortada.applications.quran.data.rest.utils.NetworkResult
import com.seifmortada.applications.quran.domain.repository.surah.SurahRepository

class SurahRepositoryImpl(
    private val recitersApiService: RecitersApi
) : SurahRepository {
    override suspend fun getAyahRecitation(
        ayahNumber: String
    ): NetworkResult<String> {
        val url = "${ApiConstant.URL_Ayah_Recitation}$ayahNumber.mp3"
        return try {
            val response = recitersApiService.getAyahRecitation(url)
            if (response.isSuccessful && response.body() != null)
                NetworkResult.Success(url)
            else NetworkResult.Error("خطأ")
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }
}
