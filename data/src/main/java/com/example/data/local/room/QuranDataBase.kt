package com.example.data.local.room
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.room.converter.VerseConverter
import com.example.data.local.room.converter.ZikrConverter
import com.example.data.local.room.dao.QuranDao
import com.example.data.local.room.dao.ZikrDao
import com.example.data.local.room.entities.azkar.AzkarItem
import com.example.data.local.room.entities.azkar.AzkarItemData
import com.example.data.local.room.entities.quran.SurahEntity
import com.example.data.local.room.entities.quran.VerseEntity

@Database(
    entities = [SurahEntity::class, VerseEntity::class, AzkarItem::class, AzkarItemData::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(value = [VerseConverter::class, ZikrConverter::class])
abstract class QuranDatabase : RoomDatabase() {
    abstract fun quranDao(): QuranDao
    abstract fun zikrDao(): ZikrDao
}
