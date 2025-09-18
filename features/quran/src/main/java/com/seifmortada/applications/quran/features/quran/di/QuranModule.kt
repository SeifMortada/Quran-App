package com.seifmortada.applications.quran.features.quran.di

import com.seifmortada.applications.quran.features.quran.presentation.chapters.QuranChaptersViewModel
import com.seifmortada.applications.quran.features.quran.domain.usecase.FetchAyahRecitationUseCase
import com.seifmortada.applications.quran.features.quran.presentation.surah.SurahViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val quranModule = module {
    single { FetchAyahRecitationUseCase(surahRepository = get()) }
    viewModel { SurahViewModel(getSurahByIdUseCase = get(), fetchAyahRecitationUseCase = get()) }
    viewModel { QuranChaptersViewModel(getQuranUseCase = get()) }
}