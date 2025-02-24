package com.seifmortada.applications.quran.presentation.features.reciter_details_feature

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
    private val moshafAdapter = ReciterMoshafAdapter()

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