package com.seifmortada.applications.quran.core.domain.model.reciter_surah_moshaf
import com.seifmortada.applications.quran.core.domain.model.MoshafModel
import kotlinx.serialization.Serializable

@Serializable
data class SurahMoshafReciter(
    val moshaf: MoshafModel,
    val surahId: Int
)
