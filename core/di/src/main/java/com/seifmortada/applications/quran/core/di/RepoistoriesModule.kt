package com.seifmortada.applications.quran.core.di

import com.seifmortada.applications.quran.core.data.repository.azkar.AzkarRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.quran.QuranRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.reciters.all_reciters.RecitersRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.reciters.surah_recitation.SurahRecitationRepositoryImpl
import com.seifmortada.applications.quran.core.data.repository.surah.SurahRepositoryImpl
import com.seifmortada.applications.quran.core.domain.repository.azkar.AzkarRepository
import com.seifmortada.applications.quran.core.domain.repository.quran.QuranRepository
import com.seifmortada.applications.quran.core.domain.repository.reciters.all_reciters.RecitersRepository
import com.seifmortada.applications.quran.core.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.seifmortada.applications.quran.core.domain.repository.surah.SurahRepository
import com.seifmortada.applications.quran.core.domain.repository.settings.SettingsRepository
import com.seifmortada.applications.quran.core.data.repository.settings.SharedPreferencesSettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val repositoryModule = module {
    //===============Repository===========//
    factory<QuranRepository> {
        QuranRepositoryImpl(
            jsonDataSource = get(),
            quranDao = get()
        )
    }
    factory<SurahRepository> {
        SurahRepositoryImpl(
            recitersApiService = get()
        )
    }
    factory<RecitersRepository> {
        RecitersRepositoryImpl(
            recitersApiService = get()
        )
    }
    factory<SurahRecitationRepository> {
        SurahRecitationRepositoryImpl(
            context = get(),
            remote = get()
        )
    }
    factory<AzkarRepository> {
        AzkarRepositoryImpl(
            azkarJsonDataSource = get(),
            zikrDao = get()
        )
    }
    factory<SettingsRepository> {
        SharedPreferencesSettingsRepository(
            context = get()
        )
    }

}
