package com.example.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.room.entities.azkar.AzkarItem
import com.example.data.local.room.entities.azkar.AzkarItemData

@Dao
interface ZikrDao {
    @Query("SELECT * FROM azkar_table")
    suspend fun getAllAzkars(): List<AzkarItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAzkars(surahs: List<AzkarItem>)

    @Query("SELECT * FROM azkar_table WHERE id = :id")
    suspend fun getZikrById(id: Int): AzkarItem

    @Query("DELETE FROM azkar_table")
    suspend fun clearAzkars()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZikrItems(zikrItemData: List<AzkarItemData>)
}
