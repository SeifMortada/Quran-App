package com.seifmortada.applications.quran.core.data.mappers

import com.seifmortada.applications.quran.core.domain.model.AzkarItemModel
import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.core.data.local.room.entities.azkar.AzkarItemData

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
