package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.ui.fragment.quran.surah.SurahViewModel
import com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters.RecitersViewModel
import com.seifmortada.applications.quran.ui.fragment.reciters.surah_telawah.SurahRecitationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule= module{
    viewModel { SurahViewModel(getSurahByIdUseCase = get(), fetchAyahRecitationUseCase = get()) }
    viewModel  { RecitersViewModel(getAllRecitersUseCase = get()) }
    viewModel { SurahRecitationViewModel(getSurahByIdUseCase = get()) }
}