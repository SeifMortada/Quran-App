package com.example.data.datasource

import com.example.data.rest.apis.RecitersApi
import com.example.data.rest.response.reciters.Reciter
import java.util.Locale

class RemoteDataSourceImpl(private val recitersApiService: RecitersApi) : RemoteDataSource {
    override suspend fun retrieveSurahRecitation(
        surahNumber: String,
        server: String
    ): Result<String> {
        return runCatching {
            val formattedSurahNumber =
                String.format(Locale.US, "%03d", surahNumber.toInt()) // "001"
            val url = "$server$formattedSurahNumber.mp3"
            val response = recitersApiService.getSurahRecitation(url)
            return if (response.isSuccessful && response.body() != null) Result.success(url)
            else Result.failure(Exception(response.message().toString()))
        }
    }
}