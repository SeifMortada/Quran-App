package com.seifmortada.applications.quran.core.domain.api

/**
 * Domain-level API interface for reciters services
 * Implementations will be provided by the data layer
 */
interface RecitersApi {
    suspend fun getAyahRecitation(ayahRecitationUrl: String): Result<String>
    suspend fun getAllReciters(recitersUrl: String): Result<String>
    suspend fun getSurahRecitation(recitationsUrl: String): Result<String>
}