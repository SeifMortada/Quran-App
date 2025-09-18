package com.seifmortada.applications.quran.features.reciter.domain.usecase

import com.seifmortada.applications.quran.core.domain.model.download.DownloadProgress
import com.seifmortada.applications.quran.core.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import kotlinx.coroutines.flow.Flow

class GetSurahRecitationUseCase(
    private val surahRecitationRepository: SurahRecitationRepository
) {
    suspend operator fun invoke(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress> {
        return surahRecitationRepository.getSurahRecitation(server, surahNumber)
    }
}