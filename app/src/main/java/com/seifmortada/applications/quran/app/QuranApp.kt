package com.seifmortada.applications.quran.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.di.repositoryModule
import com.example.di.serviceModule
import com.example.di.usecaseModule
import com.seifmortada.applications.quran.di.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

const val CHANNEL_ID = "quran_app_channel"
const val CHANNEL_NAME = "Quran App"

class QuranApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private var instance: QuranApp? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}