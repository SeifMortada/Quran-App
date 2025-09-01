package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.core.domain.repository.DownloadRepository
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import com.seifmortada.applications.quran.data.repository.DownloadRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appRepositoryModule = module {
    single<DownloadRepository> {
        DownloadRepositoryImpl(
            context = androidContext(),
            fileManager = QuranFileManager(androidContext())
        )
    }
}