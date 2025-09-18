package com.seifmortada.applications.quran.core.data.repository.quran

import com.seifmortada.applications.quran.core.data.local.data_source.LocalJsonDataSource
import com.seifmortada.applications.quran.core.data.mappers.toDomain
import com.seifmortada.applications.quran.core.data.local.room.dao.QuranDao
import com.seifmortada.applications.quran.core.domain.repository.quran.QuranRepository
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import kotlin.collections.map


class QuranRepositoryImpl(
    private val jsonDataSource: LocalJsonDataSource,
    private val quranDao: QuranDao
) : QuranRepository {
    override suspend fun getQuran(): List<SurahModel> {
        val localSurahs = quranDao.getAllSurahs()
        return localSurahs.ifEmpty {
            val fetchedSurahs = jsonDataSource.getQuranData()
            quranDao.insertSurahs(fetchedSurahs)
            fetchedSurahs
        }.map { it.toDomain() }
    }

    override suspend fun getSurahById(id: Int): SurahModel {
        return quranDao.getSurahById(id).toDomain()
    }

}
