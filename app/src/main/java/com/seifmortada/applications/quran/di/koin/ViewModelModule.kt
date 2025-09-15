package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.core.ui.QuranFileManager
import com.seifmortada.applications.quran.features.zikr.azkars.AzkarViewModel
import com.seifmortada.applications.quran.features.quran.chapters.QuranChaptersViewModel
import com.seifmortada.applications.quran.features.reciter.reciters.RecitersViewModel
import com.seifmortada.applications.quran.features.settings.SettingsViewModel
import com.seifmortada.applications.quran.features.quran.surah.SurahViewModel
import com.seifmortada.applications.quran.features.reciter.chapters.ReciterAllSurahsViewModel
import com.seifmortada.applications.quran.features.reciter.recitation.ReciterSurahRecitationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    single { QuranFileManager(androidContext()) }

    // ViewModels
    viewModel { SurahViewModel(getSurahByIdUseCase = get(), fetchAyahRecitationUseCase = get()) }
    viewModel { RecitersViewModel(getAllRecitersUseCase = get()) }
    viewModel {
        ReciterSurahRecitationViewModel(
            getSurahByIdUseCase = get(),
            getSurahRecitationUseCase = get(),
            downloadSurahUseCase = get()
        )
    }
    viewModel { ReciterAllSurahsViewModel(getQuranUseCase = get()) }
    viewModel { QuranChaptersViewModel(getQuranUseCase = get()) }
    viewModel { AzkarViewModel(getAzkarsUseCase = get()) }
    viewModel { SettingsViewModel(settingsRepository = get())}
}
