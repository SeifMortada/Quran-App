package com.seifmortada.applications.quran.data.repository.surah

import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import okhttp3.ResponseBody
import java.io.InputStream

interface SurahRepository {
    suspend fun getAyahRecitation(ayahNumber: String):NetworkResult<String>
}