package com.example.di

import com.example.domain.usecase.GetAllRecitersUseCase
import com.example.domain.usecase.GetAzkarsUseCase
import com.example.domain.usecase.GetQuranUseCase
import com.example.domain.usecase.GetSurahByIdUseCase
import org.koin.dsl.module


val usecaseModule = module {
    single { GetQuranUseCase(quranRepository = get()) }
    single { GetAzkarsUseCase(zikrRepository = get()) }
    single { GetSurahByIdUseCase(quranRepository = get()) }
    single { GetAllRecitersUseCase(recitersRepository = get()) }
}
