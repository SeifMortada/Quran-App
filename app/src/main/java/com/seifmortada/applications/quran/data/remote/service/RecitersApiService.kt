package com.seifmortada.applications.quran.data.remote.service

import com.seifmortada.applications.quran.domain.model.response.reciters.RecitersResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RecitersApiService {
    @GET
    suspend fun getAyahRecitation(@Url ayahRecitationUrl: String): ResponseBody

    @GET
    suspend fun getAllReciters(@Url recitersUrl: String): Response<RecitersResponse>
}