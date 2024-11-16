package com.example.data.repository.quran

import com.example.data.local.data_source.QuranJsonDataSource
import com.example.data.local.room.dao.QuranDao
import com.example.data.mappers.toDomain
import com.example.domain.model.SurahModel
import com.example.domain.repository.quran.QuranRepository


class QuranRepositoryImpl(
    private val jsonDataSource: QuranJsonDataSource,
    private val quranDao: QuranDao
) : QuranRepository {
    override suspend fun getQuran(): List<SurahModel> {
        val localSurahs = quranDao.getAllSurahs()
        return localSurahs.ifEmpty {
            val fetchedSurahs = jsonDataSource.getQuranFromJson()
            quranDao.insertSurahs(fetchedSurahs)
            fetchedSurahs
        }.map { it.toDomain() }
    }

    override suspend fun getSurahById(id: Int): SurahModel {
        return quranDao.getSurahById(id).toDomain()
    }

}