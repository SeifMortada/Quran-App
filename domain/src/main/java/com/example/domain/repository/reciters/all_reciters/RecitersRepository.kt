package com.example.domain.repository.reciters.all_reciters

import com.example.domain.model.NetworkResult
import com.example.domain.model.ReciterModel

interface RecitersRepository {
    suspend fun getAllReciters(): NetworkResult<List<ReciterModel>>
}