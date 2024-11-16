package com.example.data.di


import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
//=============Retrofit===============//
    single { provideOkHttpClient(androidContext()) }
    single { provideRetrofit(androidContext()) }
//===============Apis===============//
    single { provideQuranApi(androidContext()) }
    single { provideRecitersApi(androidContext()) }
}