package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.data.repository.quran.QuranRepository
import com.seifmortada.applications.quran.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.data.repository.reciters.RecitersRepository
import com.seifmortada.applications.quran.data.repository.reciters.RecitersRepositoryImpl
import com.seifmortada.applications.quran.data.repository.surah.SurahRepository
import com.seifmortada.applications.quran.data.repository.surah.SurahRepositoryImpl
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import com.seifmortada.applications.quran.ui.fragment.reciters.RecitersViewModel
import com.seifmortada.applications.quran.ui.fragment.surah.SurahViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //=============Retrofit===============//
    single { provideBaseUrl() }
    single { provideOkHttpClient() }
    single { provideGson() }
    single { provideRetrofit() }
    single { provideQuranApiService() }
    single { provideRecitersApiService() }
    //===============Room Database===========//
//    single { provideDatabase(androidContext()) }
//    single { provideQuranDao(get()) }
    //===============Repository===========//
    factory<QuranRepository> { QuranRepositoryImpl(get()) }
    factory<SurahRepository> { SurahRepositoryImpl(get()) }
    factory<RecitersRepository> { RecitersRepositoryImpl(get()) }

    //===============ViewModel===========//
    viewModel() { MainViewModel(get()) }
    viewModel() { SurahViewModel(get()) }
    viewModel() { RecitersViewModel(get()) }


}