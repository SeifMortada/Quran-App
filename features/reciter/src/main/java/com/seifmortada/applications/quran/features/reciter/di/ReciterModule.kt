package com.seifmortada.applications.quran.features.reciter.di

import com.seifmortada.applications.quran.features.reciter.data.repo.RecitersRepositoryImpl
import com.seifmortada.applications.quran.features.reciter.domain.repo.RecitersRepository
import com.seifmortada.applications.quran.features.reciter.domain.usecase.DownloadSurahUseCase
import com.seifmortada.applications.quran.features.reciter.domain.usecase.GetAllRecitersUseCase
import com.seifmortada.applications.quran.features.reciter.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.features.reciter.presentation.chapters.ReciterAllSurahsViewModel
import com.seifmortada.applications.quran.features.reciter.presentation.recitation.ReciterSurahRecitationViewModel
import com.seifmortada.applications.quran.features.reciter.presentation.reciters.RecitersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reciterModule = module {
    single<RecitersRepository> { RecitersRepositoryImpl(get()) }

    single { GetSurahRecitationUseCase(surahRecitationRepository = get()) }
    single { GetAllRecitersUseCase(recitersRepository = get()) }
    single { DownloadSurahUseCase(downloadRepository = get()) }

    viewModel { RecitersViewModel(getAllRecitersUseCase = get()) }
    viewModel {
        ReciterSurahRecitationViewModel(
            getSurahByIdUseCase = get(),
            downloadSurahUseCase = get()
        )
    }
    viewModel { ReciterAllSurahsViewModel(getQuranUseCase = get()) }
}