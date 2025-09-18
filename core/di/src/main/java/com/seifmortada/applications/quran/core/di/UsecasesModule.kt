package com.seifmortada.applications.quran.core.di

import com.seifmortada.applications.quran.core.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahByIdUseCase
import org.koin.dsl.module

val usecaseModule = module {
    single { GetQuranUseCase(quranRepository = get()) }
    single { GetSurahByIdUseCase(quranRepository = get()) }
  }
