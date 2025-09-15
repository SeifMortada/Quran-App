package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.domain.repository.quran.QuranRepository

class GetSurahByIdUseCase(private val quranRepository: QuranRepository) {
     suspend operator fun invoke(id: Int): SurahModel {
         val surah=quranRepository.getSurahById(id)
         return surah
    }
}
