package com.example.data.mappers

import com.example.data.rest.response.reciters.Reciter
import com.example.domain.model.ReciterModel

fun Reciter.toModel(): ReciterModel {
    return ReciterModel(
        date = this.date,
        id = this.id,
        letter = this.letter,
        moshaf = this.moshaf.map { it.toMoshafModel() },
        name = this.name
    )
}
