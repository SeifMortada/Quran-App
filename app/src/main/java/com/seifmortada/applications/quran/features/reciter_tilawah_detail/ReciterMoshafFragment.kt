package com.seifmortada.applications.quran.features.reciter_tilawah_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


class ReciterMoshafFragment : Fragment() {
    private val args = navArgs<ReciterMoshafFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ReciterTelawahDetailsCore(
                    onBackClick = { findNavController().navigateUp() },
                    reciter = args.value.reciterMoshaf,
                    onTelawahClick = {
                        findNavController().navigate(
                            ReciterMoshafFragmentDirections.actionReciterMoshafFragmentToReciterAllSurahsFragment(
                                it
                            )
                        )
                    }
                )
            }
        }
    }
}