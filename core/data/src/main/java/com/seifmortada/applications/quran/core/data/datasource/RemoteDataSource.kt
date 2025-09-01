package com.seifmortada.applications.quran.core.data.datasource

interface RemoteDataSource {

    suspend fun retrieveSurahRecitation(surahNumber:String,server:String): Result<String>
}
