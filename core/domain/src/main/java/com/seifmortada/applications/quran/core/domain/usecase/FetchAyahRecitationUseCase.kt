package com.seifmortada.applications.quran.core.domain.usecase

import com.seifmortada.applications.quran.core.domain.model.NetworkResult
import com.seifmortada.applications.quran.core.domain.repository.surah.SurahRepository

class FetchAyahRecitationUseCase(private val surahRepository: SurahRepository) {
    suspend operator fun invoke(ayahNumberInWholeQuran: Int): NetworkResult<String> {
        return surahRepository.getAyahRecitation(ayahNumberInWholeQuran.toString())
    }

}
