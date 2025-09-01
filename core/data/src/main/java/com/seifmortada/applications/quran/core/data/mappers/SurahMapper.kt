package com.seifmortada.applications.quran.core.data.mappers

import com.seifmortada.applications.quran.core.data.local.room.entities.quran.SurahEntity
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import timber.log.Timber

fun SurahEntity.toDomain(): SurahModel {
    if (this.verses.isNullOrEmpty()) {
        Timber.e( "SurahEntity with id ${this.id} has null or empty verses")
    }
    val versesDomain = this.verses?.map { it.toVerseDomain() } ?: emptyList()
    return SurahModel(
        id = this.id,
        name = this.name,
        totalVerses = this.total_verses,
        transliteration = this.transliteration,
        type = this.type,
        verses = versesDomain
    )
}


fun SurahModel.toEntity(): SurahEntity {
    return SurahEntity(
        id = this.id,
        name = this.name,
        total_verses = this.totalVerses,
        transliteration = this.transliteration,
        type = this.type,
        verses = this.verses.map { it.toVerseEntity() }
    )
}

