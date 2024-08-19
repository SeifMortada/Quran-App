package com.seifmortada.applications.quran.data.model.reciter_surah_moshaf

import android.os.Parcelable
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.domain.model.response.reciters.Moshaf
import kotlinx.parcelize.Parcelize

@Parcelize
data class SurahMoshafReciter(
    val moshaf: Moshaf,
    val surah:Surah
):Parcelable
