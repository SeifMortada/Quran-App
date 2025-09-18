package com.seifmortada.applications.quran.features.reciter.data.network.mappers

import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.features.reciter.data.network.response.Reciter

fun Reciter.toModel(): ReciterModel {
    return ReciterModel(
        date = this.date,
        id = this.id,
        letter = this.letter,
        moshaf = this.moshaf.map { it.toMoshafModel() },
        name = this.name
    )
}
