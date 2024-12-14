package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.presentation.features.surah_feature.SurahViewModel
import com.seifmortada.applications.quran.presentation.features.reciters_feature.RecitersViewModel
import com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature.SurahRecitationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module{
    viewModel { SurahViewModel(getSurahByIdUseCase = get(), fetchAyahRecitationUseCase = get()) }
    viewModel  { RecitersViewModel(getAllRecitersUseCase = get()) }
    viewModel { SurahRecitationViewModel(getSurahByIdUseCase = get()) }
}