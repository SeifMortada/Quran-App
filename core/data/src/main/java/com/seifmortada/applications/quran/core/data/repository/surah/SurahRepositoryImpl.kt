package com.seifmortada.applications.quran.core.data.repository.surah

import com.seifmortada.applications.quran.core.data.rest.apis.RecitersApi
import com.seifmortada.applications.quran.core.data.rest.utils.ApiConstant.URL_Ayah_Recitation
import com.seifmortada.applications.quran.core.domain.repository.surah.SurahRepository
import com.seifmortada.applications.quran.core.domain.model.NetworkResult


class SurahRepositoryImpl(
    private val recitersApiService: RecitersApi
) : SurahRepository {
    override suspend fun getAyahRecitation(
        ayahNumber: String
    ): NetworkResult<String> {
        val url = "${URL_Ayah_Recitation}$ayahNumber.mp3"
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
