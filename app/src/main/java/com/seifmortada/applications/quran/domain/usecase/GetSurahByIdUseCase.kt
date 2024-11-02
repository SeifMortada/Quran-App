package com.seifmortada.applications.quran.domain.usecase

import com.seifmortada.applications.quran.data.local.room.dao.QuranDao
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah

class GetSurahByIdUseCase(private val quranDao: QuranDao) {
     suspend operator fun invoke(id: Int): Surah {
        return quranDao.getSurahById(id)
    }
}