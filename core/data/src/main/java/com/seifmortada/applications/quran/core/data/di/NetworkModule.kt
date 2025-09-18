package com.seifmortada.applications.quran.core.data.di

import com.seifmortada.applications.quran.core.data.api.RecitersApiImpl
import com.seifmortada.applications.quran.core.domain.api.RecitersApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single { provideRetrofit(androidContext()) }
    single { provideQuranApi(androidContext()) }

    // Provide Retrofit-based RecitersApi implementation
    single { provideRecitersApi(androidContext()) }

    // Provide domain RecitersApi interface using adapter pattern
    single<RecitersApi> { RecitersApiImpl(get()) }
}
