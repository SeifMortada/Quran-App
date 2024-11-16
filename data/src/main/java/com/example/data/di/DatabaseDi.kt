package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.room.QuranDatabase

fun provideDatabase(context: Context): QuranDatabase =
    Room.databaseBuilder(context, QuranDatabase::class.java, "QuranDatabase").fallbackToDestructiveMigration().build()

fun provideQuranDao(database: QuranDatabase) = database.quranDao()

fun provideZikrDao(database: QuranDatabase) = database.zikrDao()