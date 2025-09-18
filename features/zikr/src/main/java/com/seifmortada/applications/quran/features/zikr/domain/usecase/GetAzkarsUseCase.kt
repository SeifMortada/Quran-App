package com.seifmortada.applications.quran.features.zikr.domain.usecase


import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.domain.repository.azkar.AzkarRepository

class GetAzkarsUseCase(private val zikrRepository: AzkarRepository) {
    suspend operator fun invoke(): List<AzkarModel> {
        return zikrRepository.fetchAzkars()
    }
}
