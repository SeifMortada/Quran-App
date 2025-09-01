package com.seifmortada.applications.quran.core.data.mappers

import com.seifmortada.applications.quran.core.data.rest.response.reciters.Reciter
import com.seifmortada.applications.quran.core.domain.model.ReciterModel

fun Reciter.toModel(): ReciterModel {
    return ReciterModel(
        date = this.date,
        id = this.id,
        letter = this.letter,
        moshaf = this.moshaf.map { it.toMoshafModel() },
        name = this.name
    )
}
