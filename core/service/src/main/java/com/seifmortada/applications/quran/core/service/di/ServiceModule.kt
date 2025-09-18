package com.seifmortada.applications.quran.core.service.di

import com.seifmortada.applications.quran.core.domain.repository.DownloadRepository
import com.seifmortada.applications.quran.core.service.download.DownloadRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val downloadServiceModule = module {
    single<DownloadRepository> { DownloadRepositoryImpl(context = androidContext()) }
}