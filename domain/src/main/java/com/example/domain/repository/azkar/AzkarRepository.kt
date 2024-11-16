package com.example.domain.repository.azkar

import com.example.domain.model.AzkarItemModel
import com.example.domain.model.AzkarModel

interface AzkarRepository {
    suspend fun fetchAzkars():List<AzkarModel>
}