package com.example.data.datasource

import com.seifmortada.applications.quran.core.data.datasource.RemoteDataSource
import com.seifmortada.applications.quran.core.data.rest.apis.RecitersApi
import java.util.Locale

class RemoteDataSourceImpl(private val recitersApiService: RecitersApi) : RemoteDataSource {
    override suspend fun retrieveSurahRecitation(
        surahNumber: String,
        server: String
    ): Result<String> {
        return runCatching {
            // Format surah number to 3 digits: "001", "002", etc.
            val formattedSurahNumber =
                String.format(Locale.US, "%03d", surahNumber.toInt())

            // Ensure server URL ends with slash
            val normalizedServer = if (server.endsWith("/")) server else "$server/"

            // Construct the full URL: https://server8.mp3quran.net/ahmad_huth/001.mp3
            val url = "${normalizedServer}${formattedSurahNumber}.mp3"

            val response = recitersApiService.getSurahRecitation(url)
            return if (response.isSuccessful && response.body() != null) {
                Result.success(url)
            } else {
                Result.failure(
                    Exception(
                        "Failed to retrieve recitation from $url: ${response.code()} - ${response.message()}"
                    )
                )
            }
        }.getOrElse { exception ->
            Result.failure(
                Exception(
                    "Error constructing recitation URL for surah $surahNumber from server $server: ${exception.message}",
                    exception
                )
            )
        }
    }
}
