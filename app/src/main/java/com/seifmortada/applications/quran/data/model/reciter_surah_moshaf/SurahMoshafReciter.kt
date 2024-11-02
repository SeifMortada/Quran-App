package com.seifmortada.applications.quran.data.model.reciter_surah_moshaf

import android.os.Parcelable
import com.seifmortada.applications.quran.data.rest.response.reciters.Moshaf
import kotlinx.parcelize.Parcelize

@Parcelize
data class SurahMoshafReciter(
    val moshaf: Moshaf,
    val surahId: Int
):Parcelable
