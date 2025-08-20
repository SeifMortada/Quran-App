package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.features.azkars.AzkarViewModel
import com.seifmortada.applications.quran.features.quran_chapters.QuranChaptersViewModel
import com.seifmortada.applications.quran.features.reciter_tilawah_chapters.ReciterAllSurahsViewModel
import com.seifmortada.applications.quran.features.reciters.RecitersViewModel
import com.seifmortada.applications.quran.features.surah.SurahViewModel
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.ReciterSurahRecitationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SurahViewModel(getSurahByIdUseCase = get(), fetchAyahRecitationUseCase = get()) }
    viewModel { RecitersViewModel(getAllRecitersUseCase = get()) }
    viewModel {
        ReciterSurahRecitationViewModel(
            getSurahByIdUseCase = get(),
            getSurahRecitationUseCase = get()
        )
    }
    viewModel { ReciterAllSurahsViewModel(getQuranUseCase = get()) }
    viewModel { QuranChaptersViewModel(getQuranUseCase = get()) }
    viewModel { AzkarViewModel(getAzkarsUseCase = get()) }
}