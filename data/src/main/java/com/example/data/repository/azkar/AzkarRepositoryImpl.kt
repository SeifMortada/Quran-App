package com.example.data.repository.azkar

import com.example.data.local.data_source.AzkarJsonDataSource
import com.example.data.local.room.dao.ZikrDao
import com.example.data.mappers.toAzkaModel
import com.example.domain.model.AzkarModel
import com.example.domain.repository.azkar.AzkarRepository


class AzkarRepositoryImpl(
    private val azkarJsonDataSource: AzkarJsonDataSource,
    private val zikrDao: ZikrDao
) : AzkarRepository {
    override suspend fun fetchAzkars(): List<AzkarModel> {
        val localAzkars = zikrDao.getAllAzkars()
        return localAzkars.ifEmpty {
            val fetchedAzkars = azkarJsonDataSource.getAzkarFromJson()
            zikrDao.insertAzkars(fetchedAzkars)
            fetchedAzkars
        }.map { it.toAzkaModel() }
    }

}