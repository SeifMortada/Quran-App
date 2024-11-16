package com.example.domain.usecase

import com.example.domain.model.AzkarItemModel
import com.example.domain.model.AzkarModel
import com.example.domain.repository.azkar.AzkarRepository

class GetAzkarsUseCase(private val zikrRepository: AzkarRepository) {
    suspend operator fun invoke(): List<AzkarModel> {
        return zikrRepository.fetchAzkars()
    }
}