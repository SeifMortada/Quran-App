package com.example.data.di


import org.koin.dsl.module

val networkModule = module {
//=============Retrofit===============//
    single { provideOkHttpClient() }
    single { provideGson() }
    single { provideRetrofit() }
//===============Apis===============//
    single { provideQuranApi() }
    single { provideRecitersApi() }
}