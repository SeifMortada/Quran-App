package com.seifmortada.applications.quran.data.remote

import com.seifmortada.applications.quran.domain.model.response.quran.QuranResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuranApiService {
    @GET("quran/ar")
    suspend fun getQuranInArabic(): Response<QuranResponse>
}