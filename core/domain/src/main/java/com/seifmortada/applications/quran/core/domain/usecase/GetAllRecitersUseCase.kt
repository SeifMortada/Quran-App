package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.core.domain.repository.reciters.all_reciters.RecitersRepository
import timber.log.Timber

class GetAllRecitersUseCase(private val recitersRepository: RecitersRepository) {

    suspend operator fun invoke(): NetworkResult<List<ReciterModel>> {
        val reciters = recitersRepository.getAllReciters()
        return when (reciters) {
            is NetworkResult.Success -> {
                Timber.d("GetAllRecitersUseCase, fetchAllReciters: ${reciters.data}")
                if (reciters.data.isEmpty()) {
                    NetworkResult.Error("No reciters found")
                } else NetworkResult.Success(reciters.data)
            }

            is NetworkResult.Error -> {
                Timber.d("GetAllRecitersUseCase, fetchAllReciters error: ${reciters.errorMessage}")
                NetworkResult.Error(reciters.errorMessage)
            }

            is NetworkResult.Loading -> NetworkResult.Loading
        }
    }
}
