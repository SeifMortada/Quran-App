package com.example.data.mappers

import com.example.data.local.room.entities.quran.VerseEntity
import com.example.domain.model.VerseModel

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

