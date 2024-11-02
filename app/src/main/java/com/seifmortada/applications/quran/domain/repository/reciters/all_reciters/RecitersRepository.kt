package com.seifmortada.applications.quran.domain.repository.reciters.all_reciters

import com.seifmortada.applications.quran.data.rest.utils.NetworkResult
import com.seifmortada.applications.quran.data.rest.response.reciters.RecitersResponse

interface RecitersRepository {
    suspend fun getAllReciters(): NetworkResult<RecitersResponse>
}