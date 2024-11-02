package com.seifmortada.applications.quran.domain.usecase

import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.domain.repository.quran.QuranRepository

class GetQuranUseCase(private val quranRepository: QuranRepository) {
    suspend operator fun invoke(): List<Surah> {
        return quranRepository.getQuran()
    }
}