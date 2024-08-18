package com.seifmortada.applications.quran

import android.app.Application
import com.seifmortada.applications.quran.di.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin{
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}