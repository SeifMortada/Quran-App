package com.seifmortada.applications.quran.core.data.di


import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    // Retrofit
    single { provideRetrofit(androidContext()) }

    // APIs
    single { provideQuranApi(androidContext()) }
    single { provideRecitersApi(androidContext()) }
}
