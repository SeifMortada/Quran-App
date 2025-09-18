package com.seifmortada.applications.quran.features.zikr.di

import com.seifmortada.applications.quran.features.zikr.domain.usecase.GetAzkarsUseCase
import com.seifmortada.applications.quran.features.zikr.presentation.azkars.AzkarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val zikrModule= module {

    single { GetAzkarsUseCase(zikrRepository = get()) }
    viewModel { AzkarViewModel(getAzkarsUseCase = get()) }

}