package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.features.reciter_tilawahs.ReciterAllSurahsViewModel
import com.seifmortada.applications.quran.features.reciters.RecitersViewModel
import com.seifmortada.applications.quran.features.surah_feature.SurahViewModel
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.SurahRecitationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module{
    viewModel { SurahViewModel(getSurahByIdUseCase = get(), fetchAyahRecitationUseCase = get()) }
    viewModel  { RecitersViewModel(getAllRecitersUseCase = get()) }
    viewModel { SurahRecitationViewModel(getSurahByIdUseCase = get(),getSurahRecitationUseCase = get()) }
    viewModel { ReciterAllSurahsViewModel(getQuranUseCase = get()) }
}