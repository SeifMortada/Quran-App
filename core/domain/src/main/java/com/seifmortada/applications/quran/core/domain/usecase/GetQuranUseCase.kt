package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.domain.repository.quran.QuranRepository

class GetQuranUseCase(private val quranRepository: QuranRepository) {
    suspend operator fun invoke(): List<SurahModel> {
        return quranRepository.getQuran()
    }
}
