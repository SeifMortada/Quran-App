package com.example.domain.model.reciter_surah_moshaf
import android.os.Parcelable
import com.example.domain.model.MoshafModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class SurahMoshafReciter(
    val moshaf: MoshafModel,
    val surahId: Int
)
