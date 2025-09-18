package com.seifmortada.applications.quran.core.data.rest.apis

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RecitersApi {
    @GET
    suspend fun getAyahRecitation(@Url ayahRecitationUrl: String): Response<ResponseBody>

    @GET
    suspend fun getAllReciters(@Url recitersUrl: String): Response<ResponseBody>

    @GET
    suspend fun getSurahRecitation(@Url recitationsUrl: String): Response<ResponseBody>
}