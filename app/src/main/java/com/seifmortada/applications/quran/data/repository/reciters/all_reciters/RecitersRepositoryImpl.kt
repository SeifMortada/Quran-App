package com.seifmortada.applications.quran.data.repository.reciters.all_reciters

import com.seifmortada.applications.quran.data.rest.apis.RecitersApi
import com.seifmortada.applications.quran.data.rest.utils.NetworkResult
import com.seifmortada.applications.quran.data.rest.response.reciters.RecitersResponse
import com.seifmortada.applications.quran.data.rest.utils.ApiConstant.URL_Fetch_All_Reciters
import com.seifmortada.applications.quran.domain.repository.reciters.all_reciters.RecitersRepository

class RecitersRepositoryImpl(private val recitersApiService: RecitersApi) :
    RecitersRepository {
    override suspend fun getAllReciters(): NetworkResult<RecitersResponse> {
        try {
            val response = recitersApiService.getAllReciters(URL_Fetch_All_Reciters)
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