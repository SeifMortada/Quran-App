package com.seifmortada.applications.quran.features.reciter.domain.repo

import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel

interface RecitersRepository {
    suspend fun getAllReciters(): NetworkResult<List<ReciterModel>>
}