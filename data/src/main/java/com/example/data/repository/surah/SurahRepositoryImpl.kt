package com.example.data.repository.surah
import com.example.data.rest.apis.RecitersApi
import com.example.data.rest.utils.ApiConstant.URL_Ayah_Recitation
import com.example.domain.model.NetworkResult
import com.example.domain.repository.surah.SurahRepository


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
