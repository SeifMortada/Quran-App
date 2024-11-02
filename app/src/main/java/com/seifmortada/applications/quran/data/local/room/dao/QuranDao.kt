package com.seifmortada.applications.quran.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.data.local.room.entities.quran.Verse

@Dao
interface QuranDao {
    @Query("SELECT * FROM surah_table")
    suspend fun getAllSurahs(): List<Surah>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<Surah>)

    @Query("SELECT * FROM surah_table WHERE id = :id")
    suspend fun getSurahById(id: Int): Surah

    @Query("DELETE FROM surah_table")
    suspend fun clearSurahs()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<Verse>)
}
