package com.seifmortada.applications.quran.core.data.mappers

import com.seifmortada.applications.quran.core.data.local.room.entities.quran.VerseEntity
import com.seifmortada.applications.quran.core.domain.model.VerseModel

fun VerseEntity.toVerseDomain(): VerseModel {
    return VerseModel(
        id = this.id,
        text = this.text,
        surahId = this.surahId
    )
}

fun VerseModel.toVerseEntity(): VerseEntity {
    return VerseEntity(
        id = this.id,
        text = this.text,
        surahId = this.surahId
    )
}

