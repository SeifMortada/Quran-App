package com.seifmortada.applications.quran.core.data.api

import com.seifmortada.applications.quran.core.data.rest.apis.RecitersApi as RetrofitRecitersApi
import com.seifmortada.applications.quran.core.domain.api.RecitersApi
import java.util.Locale

class RecitersApiImpl(private val retrofitApi: RetrofitRecitersApi) : RecitersApi {

    override suspend fun getAyahRecitation(ayahRecitationUrl: String): Result<String> {
        return runCatching {
            val response = retrofitApi.getAyahRecitation(ayahRecitationUrl)
            if (response.isSuccessful && response.body() != null) {
                ayahRecitationUrl // Return the URL if successful
            } else {
                throw Exception("Failed to get ayah recitation: ${response.code()} - ${response.message()}")
            }
        }
    }

    override suspend fun getAllReciters(recitersUrl: String): Result<String> {
        return runCatching {
            val response = retrofitApi.getAllReciters(recitersUrl)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.string() // Return the JSON response body
            } else {
                throw Exception("Failed to get reciters: ${response.code()} - ${response.message()}")
            }
        }
    }

    override suspend fun getSurahRecitation(recitationsUrl: String): Result<String> {
        return runCatching {
            val response = retrofitApi.getSurahRecitation(recitationsUrl)
            if (response.isSuccessful && response.body() != null) {
                recitationsUrl // Return the URL if successful
            } else {
                throw Exception("Failed to get surah recitation: ${response.code()} - ${response.message()}")
            }
        }
    }
}