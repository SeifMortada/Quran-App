package com.seifmortada.applications.quran.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.seifmortada.applications.quran.core.di.repositoryModule
import com.seifmortada.applications.quran.core.di.serviceModule
import com.seifmortada.applications.quran.core.di.usecaseModule
import com.seifmortada.applications.quran.di.koin.viewModelModule
import com.seifmortada.applications.quran.di.koin.appRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

const val CHANNEL_ID = "quran_app_channel"
const val BACKUP_CHANNEL_ID = "quran_downloads_v2"  // Backup channel ID
const val CHANNEL_NAME = "Quran App"

class QuranApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        startKoin {
            androidContext(this@QuranApplication)
            modules(
                listOf(
                    viewModelModule,
                    usecaseModule,
                    repositoryModule,
                    serviceModule,
                    appRepositoryModule,
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        try {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create the main notification channel
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    "Notifications for Quran app services including downloads and audio playback"
                setSound(null, null)
                enableVibration(false)
                enableLights(false)
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(channel)

            // Create backup channel as well
            val backupChannel = NotificationChannel(
                BACKUP_CHANNEL_ID,
                "$CHANNEL_NAME Backup",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Backup notifications for Quran app services"
                setSound(null, null)
                enableVibration(false)
                enableLights(false)
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(backupChannel)

        } catch (e: Exception) {
            Timber.e(e, "Failed to create notification channels")
        }
    }

    companion object {
        private var instance: QuranApplication? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}