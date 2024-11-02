package com.seifmortada.applications.quran.data.repository.azkar

import com.seifmortada.applications.quran.data.local.data_source.AzkarJsonDataSource
import com.seifmortada.applications.quran.data.local.room.dao.ZikrDao
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.domain.repository.azkar.AzkarRepository

class AzkarRepositoryImpl(
    private val azkarJsonDataSource: AzkarJsonDataSource,
    private val zikrDao: ZikrDao
) : AzkarRepository {
    override suspend fun fetchAzkars(): List<AzkarItem> {
        val localAzkars = zikrDao.getAllAzkars()
        return localAzkars.ifEmpty {
            val fetchedAzkars = azkarJsonDataSource.getAzkarFromJson()
            zikrDao.insertAzkars(fetchedAzkars)
            fetchedAzkars
        }
    }

}