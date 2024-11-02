package com.seifmortada.applications.quran.utils

import android.view.View
import androidx.navigation.findNavController
import com.seifmortada.applications.quran.data.rest.request.ayah.SurahAyahNumber
import java.text.Normalizer
import java.util.regex.Pattern

object FunctionsUtils {

    // Function to calculate the global Ayah number
    fun calculateGlobalAyahNumber(surahNumber: Int, ayahNumberInSurah: Int): Int {
        var globalAyahNumber = 0

        // Iterate over all Surahs before the given Surah
        for (i in 0 until surahNumber - 1) {
            globalAyahNumber += surahs[i].ayahCount
        }

        // Add the Ayah number within the current Surah
        globalAyahNumber += ayahNumberInSurah

        return globalAyahNumber
    }


     fun normalizeTextForFiltering(text: String): String {
        val nfdNormalizedString = Normalizer.normalize(text, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{M}")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

//    fun navigate(view:View,destination:Int){
//        view.findNavController().navigate(destination)
//    }

    val surahs = listOf(
        SurahAyahNumber(1, 7),    // Al-Fatiha
        SurahAyahNumber(2, 286),  // Al-Baqarah
        SurahAyahNumber(3, 200),  // Aal-E-Imran
        SurahAyahNumber(4, 176),  // An-Nisa
        SurahAyahNumber(5, 120),  // Al-Ma'idah
        SurahAyahNumber(6, 165),  // Al-An'am
        SurahAyahNumber(7, 206),  // Al-A'raf
        SurahAyahNumber(8, 75),   // Al-Anfal
        SurahAyahNumber(9, 129),  // At-Tawbah
        SurahAyahNumber(10, 109), // Yunus
        SurahAyahNumber(11, 123), // Hud
        SurahAyahNumber(12, 111), // Yusuf
        SurahAyahNumber(13, 43),  // Ar-Ra'd
        SurahAyahNumber(14, 52),  // Ibrahim
        SurahAyahNumber(15, 99),  // Al-Hijr
        SurahAyahNumber(16, 128), // An-Nahl
        SurahAyahNumber(17, 111), // Al-Isra
        SurahAyahNumber(18, 110), // Al-Kahf
        SurahAyahNumber(19, 98),  // Maryam
        SurahAyahNumber(20, 135), // Ta-Ha
        SurahAyahNumber(21, 112), // Al-Anbiya
        SurahAyahNumber(22, 78),  // Al-Hajj
        SurahAyahNumber(23, 118), // Al-Mu'minun
        SurahAyahNumber(24, 64),  // An-Nur
        SurahAyahNumber(25, 77),  // Al-Furqan
        SurahAyahNumber(26, 227), // Ash-Shu'ara
        SurahAyahNumber(27, 93),  // An-Naml
        SurahAyahNumber(28, 88),  // Al-Qasas
        SurahAyahNumber(29, 69),  // Al-Ankabut
        SurahAyahNumber(30, 60),  // Ar-Rum
        SurahAyahNumber(31, 34),  // Luqman
        SurahAyahNumber(32, 30),  // As-Sajda
        SurahAyahNumber(33, 73),  // Al-Ahzab
        SurahAyahNumber(34, 54),  // Saba
        SurahAyahNumber(35, 45),  // Fatir
        SurahAyahNumber(36, 83),  // Ya-Sin
        SurahAyahNumber(37, 182), // As-Saffat
        SurahAyahNumber(38, 88),  // Sad
        SurahAyahNumber(39, 75),  // Az-Zumar
        SurahAyahNumber(40, 85),  // Ghafir
        SurahAyahNumber(41, 54),  // Fussilat
        SurahAyahNumber(42, 53),  // Ash-Shura
        SurahAyahNumber(43, 89),  // Az-Zukhruf
        SurahAyahNumber(44, 59),  // Ad-Dukhan
        SurahAyahNumber(45, 37),  // Al-Jathiya
        SurahAyahNumber(46, 35),  // Al-Ahqaf
        SurahAyahNumber(47, 38),  // Muhammad
        SurahAyahNumber(48, 29),  // Al-Fath
        SurahAyahNumber(49, 18),  // Al-Hujurat
        SurahAyahNumber(50, 45),  // Qaf
        SurahAyahNumber(51, 60),  // Adh-Dhariyat
        SurahAyahNumber(52, 49),  // At-Tur
        SurahAyahNumber(53, 62),  // An-Najm
        SurahAyahNumber(54, 55),  // Al-Qamar
        SurahAyahNumber(55, 78),  // Ar-Rahman
        SurahAyahNumber(56, 96),  // Al-Waqi'a
        SurahAyahNumber(57, 29),  // Al-Hadid
        SurahAyahNumber(58, 22),  // Al-Mujadila
        SurahAyahNumber(59, 24),  // Al-Hashr
        SurahAyahNumber(60, 13),  // Al-Mumtahina
        SurahAyahNumber(61, 14),  // As-Saff
        SurahAyahNumber(62, 11),  // Al-Jumu'a
        SurahAyahNumber(63, 11),  // Al-Munafiqun
        SurahAyahNumber(64, 18),  // At-Taghabun
        SurahAyahNumber(65, 12),  // At-Talaq
        SurahAyahNumber(66, 12),  // At-Tahrim
        SurahAyahNumber(67, 30),  // Al-Mulk
        SurahAyahNumber(68, 52),  // Al-Qalam
        SurahAyahNumber(69, 52),  // Al-Haaqqa
        SurahAyahNumber(70, 44),  // Al-Ma'arij
        SurahAyahNumber(71, 28),  // Nuh
        SurahAyahNumber(72, 28),  // Al-Jinn
        SurahAyahNumber(73, 20),  // Al-Muzzammil
        SurahAyahNumber(74, 56),  // Al-Muddathir
        SurahAyahNumber(75, 40),  // Al-Qiyama
        SurahAyahNumber(76, 31),  // Al-Insan
        SurahAyahNumber(77, 50),  // Al-Mursalat
        SurahAyahNumber(78, 40),  // An-Naba
        SurahAyahNumber(79, 46),  // An-Nazi'at
        SurahAyahNumber(80, 42),  // Abasa
        SurahAyahNumber(81, 29),  // At-Takwir
        SurahAyahNumber(82, 19),  // Al-Infitar
        SurahAyahNumber(83, 36),  // Al-Mutaffifin
        SurahAyahNumber(84, 25),  // Al-Inshiqaq
        SurahAyahNumber(85, 22),  // Al-Buruj
        SurahAyahNumber(86, 17),  // At-Tariq
        SurahAyahNumber(87, 19),  // Al-A'la
        SurahAyahNumber(88, 26),  // Al-Ghashiya
        SurahAyahNumber(89, 30),  // Al-Fajr
        SurahAyahNumber(90, 20),  // Al-Balad
        SurahAyahNumber(91, 15),  // Ash-Shams
        SurahAyahNumber(92, 21),  // Al-Lail
        SurahAyahNumber(93, 11),  // Ad-Duha
        SurahAyahNumber(94, 8),   // Ash-Sharh
        SurahAyahNumber(95, 8),   // At-Tin
        SurahAyahNumber(96, 19),  // Al-Alaq
        SurahAyahNumber(97, 5),   // Al-Qadr
        SurahAyahNumber(98, 8),   // Al-Bayyina
        SurahAyahNumber(99, 8),   // Az-Zalzala
        SurahAyahNumber(100, 11), // Al-Adiyat
        SurahAyahNumber(101, 11), // Al-Qari'a
        SurahAyahNumber(102, 8),  // At-Takathur
        SurahAyahNumber(103, 3),  // Al-Asr
        SurahAyahNumber(104, 9),  // Al-Humaza
        SurahAyahNumber(105, 5),  // Al-Fil
        SurahAyahNumber(106, 4),  // Quraish
        SurahAyahNumber(107, 7),  // Al-Ma'un
        SurahAyahNumber(108, 3),  // Al-Kawthar
        SurahAyahNumber(109, 6),  // Al-Kafirun
        SurahAyahNumber(110, 3),  // An-Nasr
        SurahAyahNumber(111, 5),  // Al-Masad
        SurahAyahNumber(112, 4),  // Al-Ikhlas
        SurahAyahNumber(113, 5),  // Al-Falaq
        SurahAyahNumber(114, 6)   // An-Nas
    )
}