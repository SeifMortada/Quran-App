package com.seifmortada.applications.quran

import android.app.Application
import com.seifmortada.applications.quran.di.koin.appModule
import com.tazkiyatech.quran.sdk.database.QuranDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}