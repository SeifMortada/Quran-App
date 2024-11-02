package com.seifmortada.applications.quran.data.repository.quran

import com.seifmortada.applications.quran.data.local.data_source.QuranJsonDataSource
import com.seifmortada.applications.quran.data.local.room.dao.QuranDao
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.domain.repository.quran.QuranRepository

class QuranRepositoryImpl(
    private val jsonDataSource: QuranJsonDataSource,
    private val quranDao: QuranDao
) : QuranRepository {
    override suspend fun getQuran(): List<Surah> {
        val localSurahs = quranDao.getAllSurahs()
        return localSurahs.ifEmpty {
            val fetchedSurahs = jsonDataSource.getQuranFromJson()
            quranDao.insertSurahs(fetchedSurahs)
            fetchedSurahs
        }
    }

}