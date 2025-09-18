package com.seifmortada.applications.quran.core.di

import com.seifmortada.applications.quran.core.data.repository.azkar.AzkarRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.reciters.surah_recitation.SurahRecitationRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.surah.SurahRepositoryImpl
import com.seifmortada.applications.quran.core.domain.repository.azkar.AzkarRepository
import com.seifmortada.applications.quran.core.domain.repository.quran.QuranRepository
import com.seifmortada.applications.quran.core.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.seifmortada.applications.quran.core.domain.repository.surah.SurahRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<QuranRepository> { QuranRepositoryImpl(get(), get()) }
    single<SurahRecitationRepository> { SurahRecitationRepositoryImpl(get(), get())}
    single<SurahRepository> { SurahRepositoryImpl(get()) }
    single<AzkarRepository> { AzkarRepositoryImpl(get(), get()) }
}
