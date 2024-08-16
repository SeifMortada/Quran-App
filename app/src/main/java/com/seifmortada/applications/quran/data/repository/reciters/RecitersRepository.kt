package com.seifmortada.applications.quran.data.repository.reciters

import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.domain.model.response.reciters.RecitersResponse

interface RecitersRepository {
    suspend fun getAllReciters(): NetworkResult<RecitersResponse>
}