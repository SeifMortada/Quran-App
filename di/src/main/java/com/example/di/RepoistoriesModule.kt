package com.example.di

import com.example.data.repository.azkar.AzkarRepositoryImpl
import com.example.data.repository.quran.QuranRepositoryImpl
import com.example.data.repository.reciters.all_reciters.RecitersRepositoryImpl
import com.example.data.repository.reciters.surah_recitation.SurahRecitationRepositoryImpl
import com.example.data.repository.surah.SurahRepositoryImpl
import com.example.domain.repository.azkar.AzkarRepository
import com.example.domain.repository.quran.QuranRepository
import com.example.domain.repository.reciters.all_reciters.RecitersRepository
import com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.example.domain.repository.surah.SurahRepository
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
            recitersApiService = get()
        )
    }
    factory<AzkarRepository> {
        AzkarRepositoryImpl(
            azkarJsonDataSource = get(),
            zikrDao = get()
        )
    }

}
