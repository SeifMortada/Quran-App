package com.seifmortada.applications.quran.data.repository.reciters.all_reciters

import com.seifmortada.applications.quran.data.remote.service.RecitersApiService
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.domain.model.response.reciters.RecitersResponse

class RecitersRepositoryImpl(private val recitersApiService: RecitersApiService) :
    RecitersRepository {
    override suspend fun getAllReciters(): NetworkResult<RecitersResponse> {
        val url = "https://mp3quran.net/api/v3/reciters"
        try {
            val response = recitersApiService.getAllReciters(url)
            if (response.isSuccessful && response.body() != null) {
                return NetworkResult.Success(response.body()!!)
            } else {
                return NetworkResult.Error("response is null or empty")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message.toString())
        }
    }
}