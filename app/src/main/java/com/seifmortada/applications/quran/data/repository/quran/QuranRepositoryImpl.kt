package com.seifmortada.applications.quran.data.repository.quran

import androidx.lifecycle.LiveData
import com.seifmortada.applications.quran.data.api_result.NetworkResult
import com.seifmortada.applications.quran.data.local.dao.QuranDAO
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
import com.seifmortada.applications.quran.data.remote.QuranApiService
import com.seifmortada.applications.quran.domain.model.response.quran.QuranResponse

class QuranRepositoryImpl(private val quranApi: QuranApiService, private val quranDao: QuranDAO) :
    QuranRepository {
    override suspend fun getQuranInArabic(): NetworkResult<QuranResponse> {
        val response = quranApi.getQuranInArabic()
        if (response.isSuccessful) {
            response.body()?.let {
                return NetworkResult.Success(it)
            }
        } else {
            return NetworkResult.Error(response.message().toString())
        }
        return NetworkResult.Error("Something went wrong")
    }

    override suspend fun insertPages(list: List<QuranPage>) {
        try {
            quranDao.insertPages(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override  fun getAllPages(): LiveData<List<QuranPage>> {
        return quranDao.getAllPages()
    }
}