package com.example.domain.usecase

import com.example.domain.model.SurahModel
import com.example.domain.repository.quran.QuranRepository

class GetQuranUseCase(private val quranRepository: QuranRepository) {
    suspend operator fun invoke(): List<SurahModel> {
        return quranRepository.getQuran()
    }
}