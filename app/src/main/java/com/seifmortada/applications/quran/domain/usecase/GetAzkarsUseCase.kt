package com.seifmortada.applications.quran.domain.usecase

import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.domain.repository.azkar.AzkarRepository
import com.seifmortada.applications.quran.domain.repository.quran.QuranRepository

class GetAzkarsUseCase(private val zikrRepository: AzkarRepository) {
    suspend operator fun invoke(): List<AzkarItem> {
        return zikrRepository.fetchAzkars()
    }
}