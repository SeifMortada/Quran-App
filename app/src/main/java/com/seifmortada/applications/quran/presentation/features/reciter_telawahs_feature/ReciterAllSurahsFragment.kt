package com.seifmortada.applications.quran.presentation.features.reciter_telawahs_feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


class ReciterAllSurahsFragment :
    Fragment() {
    private val args = navArgs<ReciterAllSurahsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            val moshaf = args.value.moshaf
            setContent {
                ReciterAllSurahsCore(
                    onBackClicked = { findNavController().navigateUp() },
                    onSurahClicked = { surahAndTelawah ->
                        findNavController().navigate(
                            ReciterAllSurahsFragmentDirections.actionReciterAllSurahsFragmentToSurahRecitationFragment(
                                surahAndTelawah
                            )
                        )
                    }, availableSurahsWithThisTelawah = moshaf
                )
            }
        }
    }
}