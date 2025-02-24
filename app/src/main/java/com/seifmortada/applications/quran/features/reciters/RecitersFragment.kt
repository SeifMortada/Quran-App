package com.seifmortada.applications.quran.features.reciters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class RecitersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ReciterCore(
                    onBackClick = { findNavController().navigateUp() },
                    onReciterClick = { reciter->
                        val action=RecitersFragmentDirections.actionRecitersFragmentToReciterMoshafFragment(reciter)
                        findNavController().navigate(action)
                    }
                )
            }
        }
    }
}