package com.seifmortada.applications.quran.features.reciter.domain.usecase

import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.features.reciter.domain.repo.RecitersRepository

class GetAllRecitersUseCase(private val recitersRepository: RecitersRepository) {

    suspend operator fun invoke(): NetworkResult<List<ReciterModel>> {
        val reciters = recitersRepository.getAllReciters()
        return when (reciters) {
            is NetworkResult.Success -> {
                if (reciters.data.isEmpty()) {
                    NetworkResult.Error("No reciters found")
                } else NetworkResult.Success(reciters.data)
            }

            is NetworkResult.Error -> {
                NetworkResult.Error(reciters.errorMessage)
            }

            is NetworkResult.Loading -> NetworkResult.Loading
        }
    }
}