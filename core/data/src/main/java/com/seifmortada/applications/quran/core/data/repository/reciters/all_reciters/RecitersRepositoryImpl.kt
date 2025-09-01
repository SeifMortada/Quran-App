package com.seifmortada.applications.quran.core.data.repository.reciters.all_reciters

import com.seifmortada.applications.quran.core.data.mappers.toModel
import com.seifmortada.applications.quran.core.data.rest.apis.RecitersApi
import com.seifmortada.applications.quran.core.data.rest.utils.ApiConstant.URL_Fetch_All_Reciters
import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.core.domain.repository.reciters.all_reciters.RecitersRepository


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
