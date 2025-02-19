package com.example.domain.usecase

import android.util.Log
import com.example.domain.model.NetworkResult
import com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository

class GetSurahRecitationUseCase(private val surahRecitationRepository: SurahRecitationRepository) {
    suspend operator fun invoke(server: String, surahNumber: String): NetworkResult<String> {
      val result=  surahRecitationRepository.getSurahRecitation(server, surahNumber)
        Log.d("GetSurahRecitationUseCase", "invoke: $result")
        return result
    }

}
