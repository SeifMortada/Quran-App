package com.example.data.mappers

import com.example.data.local.room.entities.azkar.AzkarItem
import com.example.data.local.room.entities.azkar.AzkarItemData
import com.example.domain.model.AzkarItemModel
import com.example.domain.model.AzkarModel

fun AzkarItem.toAzkaModel(): AzkarModel {
    return AzkarModel(
        array = this.array.map { it.toAzkarItemModel() },
        audio = this.audio,
        category = this.category,
        filename = this.filename,
        id = this.id
    )
}

fun AzkarItemData.toAzkarItemModel(): AzkarItemModel {
    return AzkarItemModel(
        audio = this.audio,
        count = this.count,
        filename = this.filename,
        id = this.id,
        text = this.text
    )
}
