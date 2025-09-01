package com.seifmortada.applications.quran.core.domain.repository.reciters.all_reciters

import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel

interface RecitersRepository {
    suspend fun getAllReciters(): NetworkResult<List<ReciterModel>>
}
