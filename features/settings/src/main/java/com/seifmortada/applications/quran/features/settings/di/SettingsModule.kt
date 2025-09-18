package com.seifmortada.applications.quran.features.settings.di

import com.seifmortada.applications.quran.features.settings.presentation.SettingsViewModel
import com.seifmortada.applications.quran.features.settings.data.repo.SharedPreferencesSettingsRepository
import com.seifmortada.applications.quran.features.settings.domain.usecase.GetAppSettingsUseCase
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsRepository> { SharedPreferencesSettingsRepository(get()) }

    single { GetAppSettingsUseCase(settingsRepository = get()) }

    viewModel { SettingsViewModel(settingsRepository = get())}

}