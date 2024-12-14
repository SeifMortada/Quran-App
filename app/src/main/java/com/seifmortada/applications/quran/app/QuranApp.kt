package com.seifmortada.applications.quran.app

import android.app.Application
import android.content.Context
import com.example.di.repositoryModule
import com.example.di.serviceModule
import com.example.di.usecaseModule
import com.seifmortada.applications.quran.di.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class QuranApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@QuranApp)
            modules(
                listOf(
                    viewModelModule,
                    usecaseModule,
                    repositoryModule,
                    serviceModule,
                )
            )
        }
    }

    companion object {
        private var instance: QuranApp? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}