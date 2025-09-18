package com.seifmortada.applications.quran.core.data.datasource

import com.seifmortada.applications.quran.core.data.datasource.RemoteDataSource
import com.seifmortada.applications.quran.core.domain.api.RecitersApi
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

            val result = recitersApiService.getSurahRecitation(url)
            return if (result.isSuccess) {
                Result.success(url)
            } else {
                Result.failure(
                    Exception(
                        "Failed to retrieve recitation from $url: ${result.exceptionOrNull()?.message}"
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
