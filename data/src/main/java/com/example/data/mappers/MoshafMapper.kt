package com.example.data.mappers

import com.example.data.rest.response.reciters.Moshaf
import com.example.domain.model.MoshafModel

fun Moshaf.toMoshafModel(): MoshafModel {
    return MoshafModel(
        id = this.id,
        moshafType = this.moshafType,
        name = this.name,
        server = this.server,
        surahList = this.surahList,
        surahTotal = this.surahTotal
    )
}