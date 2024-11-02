package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.data.local.data_source.AzkarJsonDataSource
import com.seifmortada.applications.quran.data.local.data_source.QuranJsonDataSource
import com.seifmortada.applications.quran.data.repository.azkar.AzkarRepositoryImpl
import com.seifmortada.applications.quran.domain.repository.quran.QuranRepository
import com.seifmortada.applications.quran.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.domain.repository.reciters.all_reciters.RecitersRepository
import com.seifmortada.applications.quran.data.repository.reciters.all_reciters.RecitersRepositoryImpl
import com.seifmortada.applications.quran.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.seifmortada.applications.quran.data.repository.reciters.surah_recitation.SurahRecitationRepositoryImpl
import com.seifmortada.applications.quran.domain.repository.surah.SurahRepository
import com.seifmortada.applications.quran.data.repository.surah.SurahRepositoryImpl
import com.seifmortada.applications.quran.domain.repository.azkar.AzkarRepository
import com.seifmortada.applications.quran.domain.usecase.GetAzkarsUseCase
import com.seifmortada.applications.quran.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.domain.usecase.GetSurahByIdUseCase
import com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters.RecitersViewModel
import com.seifmortada.applications.quran.ui.fragment.quran.surah.SurahViewModel
import com.seifmortada.applications.quran.ui.fragment.reciters.surah_telawah.SurahRecitationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //=============Retrofit===============//
    single { provideOkHttpClient() }
    single { provideGson() }
    single { provideRetrofit() }
    //===============Apis===============//
    single { provideQuranApi() }
    single { provideRecitersApi() }
    //===============Room Database===========//
    single { provideDatabase(androidContext()) }
    single { provideQuranDao(get()) }
    single { provideZikrDao(get()) }
    //===============Repository===========//
    factory<QuranRepository> { QuranRepositoryImpl(get(), get()) }
    factory<SurahRepository> { SurahRepositoryImpl(get()) }
    factory<RecitersRepository> { RecitersRepositoryImpl(get()) }
    factory<SurahRecitationRepository> { SurahRecitationRepositoryImpl(get()) }
    factory<AzkarRepository> { AzkarRepositoryImpl(get(), get()) }
    //===================UseCases================//
    single { GetQuranUseCase(get()) }
    single { GetSurahByIdUseCase(get()) }
    single {GetAzkarsUseCase(get())}
    //==============DataSources===========//
    single { QuranJsonDataSource(get()) }
    single { AzkarJsonDataSource(get()) }
    //===============ViewModel===========//
    viewModel { SurahViewModel(get(), get()) }
    viewModel { RecitersViewModel(get()) }
    viewModel { SurahRecitationViewModel(get(), get()) }


}