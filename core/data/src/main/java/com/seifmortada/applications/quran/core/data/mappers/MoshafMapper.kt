package com.seifmortada.applications.quran.core.data.mappers

import com.seifmortada.applications.quran.core.data.rest.response.reciters.Moshaf
import com.seifmortada.applications.quran.core.domain.model.MoshafModel

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
