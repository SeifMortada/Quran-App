package com.example.data.repository.reciters.all_reciters

import com.example.data.mappers.toModel
import com.example.data.rest.apis.RecitersApi
import com.example.data.rest.utils.ApiConstant.URL_Fetch_All_Reciters
import com.example.domain.model.NetworkResult
import com.example.domain.model.ReciterModel
import com.example.domain.repository.reciters.all_reciters.RecitersRepository


class RecitersRepositoryImpl(private val recitersApiService: RecitersApi) :
    RecitersRepository {
    override suspend fun getAllReciters(): NetworkResult<List<ReciterModel>> {
        try {
            val response = recitersApiService.getAllReciters(URL_Fetch_All_Reciters)
            if (response.isSuccessful && response.body() != null) {
                val reciters = response.body()
                return NetworkResult.Success(reciters!!.reciters.map { it.toModel() })
            } else {
                return NetworkResult.Error("response is null or empty")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message.toString())
        }
    }
}