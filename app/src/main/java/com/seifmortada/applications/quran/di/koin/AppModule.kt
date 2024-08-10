package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.data.repository.quran.QuranRepository
import com.seifmortada.applications.quran.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.ui.activity.BaseViewModel
import com.seifmortada.applications.quran.ui.fragment.quran_fragment.QuranFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //=============Retrofit===============//
    single { provideBaseUrl() }
    single { provideOkHttpClient() }
    single { provideGson() }
    single { provideRetrofit() }
    single { provideQuranApiService() }
    //===============Room Database===========//
    single { provideDatabase(androidContext()) }
    single { provideQuranDao(get()) }
    //===============Repository===========//
    factory<QuranRepository> { QuranRepositoryImpl(get(),get()) }
    //===============ViewModel===========//
    viewModel(){BaseViewModel(androidContext())}
    viewModel() { QuranFragmentViewModel(get(),androidContext())}

}