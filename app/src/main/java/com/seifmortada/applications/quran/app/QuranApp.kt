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
                    appRepositoryModule,
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        android.util.Log.d("QuranApp", "=== Application: createNotificationChannel START ===")
        android.util.Log.d("QuranApp", "Channel ID: $CHANNEL_ID")
        android.util.Log.d("QuranApp", "Channel Name: $CHANNEL_NAME")
        android.util.Log.d(
            "QuranApp",
            "Using IMPORTANCE_DEFAULT: ${NotificationManager.IMPORTANCE_DEFAULT}"
        )

        try {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Check if channel already exists
            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel != null) {
                android.util.Log.d("QuranApp", "=== Found existing channel ===")
                android.util.Log.d(
                    "QuranApp",
                    "Existing channel importance: ${existingChannel.importance}"
                )

                if (existingChannel.importance != NotificationManager.IMPORTANCE_DEFAULT) {
                    android.util.Log.w(
                        "QuranApp",
                        "=== CRITICAL: Channel has wrong importance! Deleting and recreating ==="
                    )
                    android.util.Log.w(
                        "QuranApp",
                        "Wrong importance: ${existingChannel.importance}, Required: ${NotificationManager.IMPORTANCE_DEFAULT}"
                    )

                    // Delete the existing channel with wrong importance
                    notificationManager.deleteNotificationChannel(CHANNEL_ID)
                    android.util.Log.d(
                        "QuranApp",
                        "=== Deleted existing channel with wrong importance ==="
                    )
                } else {
                    android.util.Log.d(
                        "QuranApp",
                        "=== Existing channel has correct importance, keeping it ==="
                    )
                    return
                }
            }

            android.util.Log.d("QuranApp", "=== Creating new notification channel ===")
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT  // CRITICAL: Must be IMPORTANCE_DEFAULT for foreground services
            ).apply {
                description = "Notifications for Quran app services including downloads"
                setSound(null, null)
                enableVibration(false)
                enableLights(false)
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            android.util.Log.d("QuranApp", "=== About to create notification channel ===")
            notificationManager.createNotificationChannel(channel)
            android.util.Log.d("QuranApp", "=== Notification channel created successfully ===")

            // Verify creation with correct importance
            val verifyChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (verifyChannel != null) {
                android.util.Log.d("QuranApp", "=== Verification: Channel exists ===")
                android.util.Log.d("QuranApp", "Verified channel name: ${verifyChannel.name}")
                android.util.Log.d(
                    "QuranApp",
                    "Verified channel importance: ${verifyChannel.importance}"
                )

                if (verifyChannel.importance == NotificationManager.IMPORTANCE_DEFAULT) {
                    android.util.Log.d(
                        "QuranApp",
                        "=== SUCCESS: Channel has correct IMPORTANCE_DEFAULT! ==="
                    )
                } else {
                    android.util.Log.e(
                        "QuranApp",
                        "=== CRITICAL ERROR: Channel still has wrong importance after recreation! ==="
                    )
                    android.util.Log.e(
                        "QuranApp",
                        "This might be due to user notification settings or system restrictions"
                    )
                    android.util.Log.e(
                        "QuranApp",
                        "Channel importance: ${verifyChannel.importance}, Expected: ${NotificationManager.IMPORTANCE_DEFAULT}"
                    )
                    // Create a backup channel with a different ID
                    val backupChannel = NotificationChannel(
                        BACKUP_CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = "Notifications for Quran app services including downloads"
                        setSound(null, null)
                        enableVibration(false)
                        enableLights(false)
                        setShowBadge(false)
                        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    }
                    notificationManager.createNotificationChannel(backupChannel)
                    android.util.Log.d(
                        "QuranApp",
                        "=== Created backup notification channel with ID: $BACKUP_CHANNEL_ID ==="
                    )
                }
            } else {
                android.util.Log.e("QuranApp", "=== ERROR: Channel verification failed ===")
            }

        } catch (e: Exception) {
            android.util.Log.e(
                "QuranApp",
                "=== CRITICAL: Exception creating notification channel ===",
                e
            )
            throw e
        }

        android.util.Log.d("QuranApp", "=== Application: createNotificationChannel COMPLETE ===")
    }

    companion object {
        private var instance: QuranApp? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}