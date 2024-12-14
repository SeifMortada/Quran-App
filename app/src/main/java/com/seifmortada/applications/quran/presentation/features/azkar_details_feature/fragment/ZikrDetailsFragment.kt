package com.seifmortada.applications.quran.presentation.features.azkar_details_feature.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.presentation.features.azkar_details_feature.composables.AzkarScreen


class ZikrDetailsFragment : Fragment() {

    val args = navArgs<ZikrDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            val azakrs = args.value.zikrItem
            setContent {
                AzkarScreen(
                    azkars = azakrs,
                    onBackButtonClicked = { findNavController().navigateUp() })
            }
        }
    }

}