package com.seifmortada.applications.quran.core.data.local.room.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seifmortada.applications.quran.core.data.local.room.entities.quran.SurahEntity
import com.seifmortada.applications.quran.core.data.local.room.entities.quran.VerseEntity

@Dao
interface QuranDao {
    @Query("SELECT * FROM surah_table")
    suspend fun getAllSurahs(): List<SurahEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahEntities: List<SurahEntity>)

    @Query("SELECT * FROM surah_table WHERE id = :id")
    suspend fun getSurahById(id: Int): SurahEntity

    @Query("DELETE FROM surah_table")
    suspend fun clearSurahs()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(vers: List<VerseEntity>)
}
