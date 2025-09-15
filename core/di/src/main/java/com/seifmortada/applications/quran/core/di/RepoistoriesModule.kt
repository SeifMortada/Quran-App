package com.seifmortada.applications.quran.core.di

import com.seifmortada.applications.quran.core.data.repository.azkar.AzkarRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.reciters.all_reciters.RecitersRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.reciters.surah_recitation.SurahRecitationRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.settings.SharedPreferencesSettingsRepository
import com.seifmortada.applications.quran.core.domain.repository.azkar.AzkarRepository
import com.seifmortada.applications.quran.core.domain.repository.quran.QuranRepository
import com.seifmortada.applications.quran.core.domain.repository.reciters.all_reciters.RecitersRepository
import com.seifmortada.applications.quran.core.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<QuranRepository> { QuranRepositoryImpl(get(), get()) }
    single<RecitersRepository> { RecitersRepositoryImpl(get()) }
    single<SurahRecitationRepository> {
        SurahRecitationRepositoryImpl(get(), get())
    }

    // Repository
    single<AzkarRepository> { AzkarRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SharedPreferencesSettingsRepository(get()) }
}
