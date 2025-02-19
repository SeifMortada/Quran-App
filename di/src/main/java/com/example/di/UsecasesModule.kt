package com.example.di

import com.example.domain.usecase.FetchAyahRecitationUseCase
import com.example.domain.usecase.GetAllRecitersUseCase
import com.example.domain.usecase.GetAzkarsUseCase
import com.example.domain.usecase.GetQuranUseCase
import com.example.domain.usecase.GetSurahByIdUseCase
import com.example.domain.usecase.GetSurahRecitationUseCase
import org.koin.dsl.module


val usecaseModule = module {
    single { GetQuranUseCase(quranRepository = get()) }
    single { GetAzkarsUseCase(zikrRepository = get()) }
    single { GetSurahByIdUseCase(quranRepository = get()) }
    single { GetAllRecitersUseCase(recitersRepository = get()) }
    single{ FetchAyahRecitationUseCase(surahRepository = get()) }
    single { GetSurahRecitationUseCase(surahRecitationRepository = get()) }
}
