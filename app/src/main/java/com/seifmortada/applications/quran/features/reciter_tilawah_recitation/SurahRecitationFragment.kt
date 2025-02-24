package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme

class SurahRecitationFragment : Fragment() {
    private val args = navArgs<SurahRecitationFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            val moshaf = args.value.surahMoshaf.moshaf
            val surahId = args.value.surahMoshaf.surahId
            val server = moshaf.server
            setContent {
                QuranAppTheme {
                    ReciterSurahRecitationRoute(
                        onBackClicked = { findNavController().navigateUp() },
                        surahId = surahId,
                        server = server
                    )
                }
            }
        }
    }
}