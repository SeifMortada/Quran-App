package com.seifmortada.applications.quran.core.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single { provideRetrofit(androidContext()) }
    single { provideQuranApi(androidContext()) }
    single { provideRecitersApi(androidContext()) }
}
