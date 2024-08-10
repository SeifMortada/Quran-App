package com.seifmortada.applications.quran.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seifmortada.applications.quran.data.local.dao.QuranDAO
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
import com.seifmortada.applications.quran.utils.Converters

@Database(entities = [QuranPage::class], version = 1)
@TypeConverters(Converters::class)
abstract class QuranDatabase : RoomDatabase(){
    abstract fun quranDAO(): QuranDAO
}