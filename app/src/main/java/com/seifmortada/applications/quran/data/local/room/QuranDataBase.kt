package com.seifmortada.applications.quran.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seifmortada.applications.quran.data.local.room.dao.QuranDao
import com.seifmortada.applications.quran.data.local.room.dao.ZikrDao
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItemData
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.data.local.room.entities.quran.Verse
import com.seifmortada.applications.quran.utils.VerseConverter
import com.seifmortada.applications.quran.utils.ZikrConverter

@Database(
    entities = [Surah::class, Verse::class, AzkarItem::class, AzkarItemData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [VerseConverter::class, ZikrConverter::class])
abstract class QuranDatabase : RoomDatabase() {
    abstract fun quranDao(): QuranDao
    abstract fun zikrDao(): ZikrDao
}
