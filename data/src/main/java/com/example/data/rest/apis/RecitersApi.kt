package com.example.data.rest.apis

import com.example.data.rest.response.reciters.RecitersResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RecitersApi {
    @GET
    suspend fun getAyahRecitation(@Url ayahRecitationUrl: String):Response< ResponseBody>

    @GET
    suspend fun getAllReciters(@Url recitersUrl: String): Response<RecitersResponse>

    @GET
    suspend fun getSurahRecitation(@Url recitationsUrl: String): Response<ResponseBody>
}