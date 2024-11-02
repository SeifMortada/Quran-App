package com.seifmortada.applications.quran.domain.repository.azkar

import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem

interface AzkarRepository {
    suspend fun fetchAzkars():List<AzkarItem>
}