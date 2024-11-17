package com.example.domain.usecase

import com.example.domain.repository.surah.SurahRepository

class FetchAyahRecitationUseCase(private val surahRepository: SurahRepository) {
    suspend operator fun invoke(ayahNumberInWholeQuran: Int): NetworkResult<String> {
        return surahRepository.getAyahRecitation(ayahNumberInWholeQuran.toString())
    }

}