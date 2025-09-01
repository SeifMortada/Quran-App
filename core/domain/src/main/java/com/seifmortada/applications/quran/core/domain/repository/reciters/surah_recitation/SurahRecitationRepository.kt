package com.seifmortada.applications.quran.core.domain.repository.reciters.surah_recitation

import com.seifmortada.applications.quran.core.domain.usecase.DownloadProgress
import kotlinx.coroutines.flow.Flow


interface SurahRecitationRepository {
    suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress>
}
