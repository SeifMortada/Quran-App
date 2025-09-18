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
import com.seifmortada.applications.quran.core.service.di.downloadServiceModule
import com.seifmortada.applications.quran.di.koin.appModule
import com.seifmortada.applications.quran.features.quran.di.quranModule
import com.seifmortada.applications.quran.features.reciter.di.reciterModule
import com.seifmortada.applications.quran.features.settings.di.settingsModule
import com.seifmortada.applications.quran.features.zikr.di.zikrModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

const val CHANNEL_ID = "quran_app_channel"
const val BACKUP_CHANNEL_ID = "quran_downloads_v2"
const val CHANNEL_NAME = "Quran App"

class QuranApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        startKoin {
            androidContext(this@QuranApplication)
            modules(
                listOf(
                    usecaseModule,
                    repositoryModule,
                    serviceModule,
                    appModule,
                    downloadServiceModule,
                    quranModule,
                    reciterModule,
                    settingsModule,
                    zikrModule
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
}