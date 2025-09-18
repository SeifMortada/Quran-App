package com.seifmortada.applications.quran.features.reciter.data.repo

import com.google.gson.Gson
import com.seifmortada.applications.quran.core.domain.api.RecitersApi
import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.features.reciter.data.network.mappers.toModel
import com.seifmortada.applications.quran.features.reciter.data.network.response.RecitersResponse
import com.seifmortada.applications.quran.features.reciter.domain.repo.RecitersRepository

class RecitersRepositoryImpl(private val recitersApiService: RecitersApi) :
    RecitersRepository {
    companion object {
        const val ALL_RECITERS_URL = "https://mp3quran.net/api/v3/reciters"

    }

    override suspend fun getAllReciters(): NetworkResult<List<ReciterModel>> {
        try {
            val result = recitersApiService.getAllReciters(ALL_RECITERS_URL)
            return if (result.isSuccess) {
                val responseBody = result.getOrThrow()
                val recitersResponse = Gson().fromJson(responseBody, RecitersResponse::class.java)
                NetworkResult.Success(recitersResponse.reciters.map { it.toModel() })
            } else {
                NetworkResult.Error("Failed to get reciters: ${result.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message.toString())
        }
    }
}