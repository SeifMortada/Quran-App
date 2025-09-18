package com.seifmortada.applications.quran.core.data.repository.azkar

import com.seifmortada.applications.quran.core.data.local.data_source.LocalJsonDataSource
import com.seifmortada.applications.quran.core.data.mappers.toAzkaModel
import com.seifmortada.applications.quran.core.data.local.room.dao.ZikrDao
import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.domain.repository.azkar.AzkarRepository


class AzkarRepositoryImpl(
    private val localJsonDataSource: LocalJsonDataSource,
    private val zikrDao: ZikrDao
) : AzkarRepository {
    override suspend fun fetchAzkars(): List<AzkarModel> {
        val localAzkars = zikrDao.getAllAzkars()
        return localAzkars.ifEmpty {
            val fetchedAzkars = localJsonDataSource.getAzkarData()
            zikrDao.insertAzkars(fetchedAzkars)
            fetchedAzkars
        }.map { it.toAzkaModel() }
    }

}
