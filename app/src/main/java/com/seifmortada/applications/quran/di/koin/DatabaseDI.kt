package com.seifmortada.applications.quran.di.koin

import android.content.Context
import androidx.room.Room
import com.seifmortada.applications.quran.data.local.room.QuranDatabase

fun provideDatabase(context: Context): QuranDatabase =
    Room.databaseBuilder(context, QuranDatabase::class.java, "QuranDatabase").build()

fun provideQuranDao(database: QuranDatabase) = database.quranDao()

fun provideZikrDao(database: QuranDatabase) = database.zikrDao()