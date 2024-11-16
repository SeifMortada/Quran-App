package com.example.domain.usecase

import com.example.domain.model.SurahModel
import com.example.domain.repository.quran.QuranRepository
import timber.log.Timber

class GetSurahByIdUseCase(private val quranRepository: QuranRepository) {
     suspend operator fun invoke(id: Int): SurahModel {
         val surah=quranRepository.getSurahById(id)
         Timber.d("Surah by id $id surah is $surah")
         return surah
    }
}