package com.seifmortada.applications.quran.domain.model.response.quran

import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage

data class QuranResponse(
    val code: Int,
    val `data`: Data,
    val status: String
) {
    companion object {
        fun convertToQuranPage(quranResponse: QuranResponse): MutableList<QuranPage> {
            val quranPages = mutableListOf<QuranPage>()
            quranResponse.data.surahs.forEach { surah ->
                val ayahsByPage = surah.ayahs.groupBy { it.page }
                ayahsByPage.forEach { (pageNumber, ayahs) ->
                    val hizbInfo = "Hizb ${ayahs.first().hizbQuarter}"
                    quranPages.add(QuranPage(pageNumber = pageNumber, hizbInfo =  hizbInfo, ayahs =  ayahs))
                }
            }
            return quranPages
        }
        fun getAllSurahs(quranResponse: QuranResponse):List<Surah>{
         return  quranResponse.data.surahs
        }
    }
}