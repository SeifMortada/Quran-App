package com.seifmortada.applications.quran.data.repository.quran

import androidx.lifecycle.LiveData
import com.seifmortada.applications.quran.data.api_result.NetworkResult
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
import com.seifmortada.applications.quran.domain.model.response.quran.QuranResponse

interface QuranRepository{
    suspend fun getQuranInArabic(): NetworkResult<QuranResponse>

    suspend fun insertPages(list: List<QuranPage>)

     fun getAllPages():LiveData<List<QuranPage>>
}