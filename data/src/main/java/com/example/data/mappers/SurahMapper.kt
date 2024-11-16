package com.example.data.mappers

import com.example.data.local.room.entities.quran.SurahEntity
import com.example.domain.model.SurahModel

fun SurahEntity.toDomain(): SurahModel {
    return SurahModel(
        id = this.id,
        name = this.name,
        totalVerses = this.total_verses,
        transliteration = this.transliteration,
        type = this.type,
        verses = this.vers.map { it.toVerseDomain() }
    )
}

fun SurahModel.toEntity(): SurahEntity {
    return SurahEntity(
        id = this.id,
        name = this.name,
        total_verses = this.totalVerses,
        transliteration = this.transliteration,
        type = this.type,
        vers = this.verses.map { it.toVerseEntity() }
    )
}

