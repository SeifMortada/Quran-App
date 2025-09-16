package com.seifmortada.applications.quran.core.data.di

import androidx.room.Room
import com.seifmortada.applications.quran.core.data.local.data_source.AzkarJsonDataSource
import com.seifmortada.applications.quran.core.data.local.data_source.QuranJsonDataSource
import com.seifmortada.applications.quran.core.data.local.room.QuranDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { androidContext().assets }
    single {
        Room.databaseBuilder(androidContext(), QuranDatabase::class.java, "quran_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<QuranDatabase>().quranDao() }
    single { get<QuranDatabase>().zikrDao() }
    single { QuranJsonDataSource(get()) }
    single { AzkarJsonDataSource(get()) }
}
