package com.seifmortada.applications.quran.core.domain.repository.azkar

import com.seifmortada.applications.quran.core.domain.model.AzkarModel

interface AzkarRepository {
    suspend fun fetchAzkars():List<AzkarModel>
}
