package com.seifmortada.applications.quran.di.koin

import com.seifmortada.applications.quran.core.ui.QuranFileManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { QuranFileManager(androidContext()) }
}