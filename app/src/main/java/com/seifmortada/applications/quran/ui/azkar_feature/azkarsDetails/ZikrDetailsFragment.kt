package com.seifmortada.applications.quran.ui.azkar_feature.azkarsDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.ui.azkar_feature.AzkarScreen


class ZikrDetailsFragment : Fragment() {

    val args = navArgs<ZikrDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            val azakrs = args.value.zikrItem.array
            setContent {
                AzkarScreen(azkars = azakrs)
            }
        }
    }

}