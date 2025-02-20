package com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.model.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentSurahRecitationBinding
import com.seifmortada.applications.quran.presentation.core.BaseFragment
import com.seifmortada.applications.quran.presentation.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature.composables.ReciterSurahRecitationCore
import com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature.composables.ReciterSurahRecitationScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class SurahRecitationFragment : Fragment() {

    private val args = navArgs<SurahRecitationFragmentArgs>()

    private val viewModel: SurahRecitationViewModel by inject()

    private lateinit var surahRecitationsAdapter: SurahRecitationAdapter

    private var exoPlayer: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            val moshaf = args.value.surahMoshaf.moshaf
            val surahId = args.value.surahMoshaf.surahId
            val server = moshaf.server
            viewModel.fetchRecitation(server, surahId)
            setContent {
                QuranAppTheme {
                    ReciterSurahRecitationCore(
                        onBackClicked = { findNavController().navigateUp() }
                    )
                }
            }
        }
    }
}