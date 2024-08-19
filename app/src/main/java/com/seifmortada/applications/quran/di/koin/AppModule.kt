package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.data.repository.quran.QuranRepository
import com.seifmortada.applications.quran.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.data.repository.reciters.all_reciters.RecitersRepository
import com.seifmortada.applications.quran.data.repository.reciters.all_reciters.RecitersRepositoryImpl
import com.seifmortada.applications.quran.data.repository.reciters.surah_recitation.SurahRecitationRepository
import com.seifmortada.applications.quran.data.repository.reciters.surah_recitation.SurahRecitationRepositoryImpl
import com.seifmortada.applications.quran.data.repository.surah.SurahRepository
import com.seifmortada.applications.quran.data.repository.surah.SurahRepositoryImpl
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters.RecitersViewModel
import com.seifmortada.applications.quran.ui.fragment.quran.surah.SurahViewModel
import com.seifmortada.applications.quran.ui.fragment.reciters.surah_recitation.SurahRecitationViewModel
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
    factory<SurahRecitationRepository> { SurahRecitationRepositoryImpl(get()) }

    //===============ViewModel===========//
    viewModel { MainViewModel(get()) }
    viewModel { SurahViewModel(get()) }
    viewModel { RecitersViewModel(get()) }
    viewModel { SurahRecitationViewModel(get()) }


}