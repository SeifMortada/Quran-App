package com.seifmortada.applications.quran.features.reciter.data.network.mappers

import com.seifmortada.applications.quran.core.domain.model.MoshafModel
import com.seifmortada.applications.quran.features.reciter.data.network.response.Moshaf

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
