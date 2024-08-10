package com.seifmortada.applications.quran.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage

@Dao
interface QuranDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pages: List<QuranPage>)

    @Query("SELECT * FROM quran_table")
    fun getAllPages(): LiveData<List<QuranPage>>
}