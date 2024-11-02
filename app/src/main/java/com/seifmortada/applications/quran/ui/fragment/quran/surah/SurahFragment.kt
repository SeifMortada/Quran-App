package com.seifmortada.applications.quran.ui.fragment.quran.surah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.quran.data.rest.utils.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentSurahBinding
import com.seifmortada.applications.quran.ui.core.BaseFragment
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.navigation.fragment.findNavController
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.utils.SearchUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SurahFragment : BaseFragment<FragmentSurahBinding, SurahViewModel>() {

    private val args by navArgs<SurahFragmentArgs>()
    override val viewModel: SurahViewModel by inject()

    private lateinit var surahAdapter: SurahAdapter
    private var exoPlayer: ExoPlayer? = null

    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSurahBinding {
        return FragmentSurahBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getSurahById(args.surahId)
        observeSurahResponseAndInitializeAdapter()
        initializeClickListeners()
        observeStates()

        observeAyahRecitationResponse()

        // Initialize ExoPlayer
        exoPlayer =
            ExoPlayer.Builder(requireContext()).build().apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            viewModel.ayahEnded.value = true
                        }
                    }
                })
            }
        viewModel.exoPlayer = exoPlayer!!

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeSurahResponseAndInitializeAdapter() {
        viewModel.surah.observe(viewLifecycleOwner) {
            if (it != null) {
                surahAdapter = SurahAdapter(viewModel, it, viewLifecycleOwner)
                surahAdapter.submitList(it.verses)
                initializeRv()
                initializeViews(it)
            }
        }
    }

    private fun initializeViews(surah: Surah) {
        binding.searchItemLayout.surahItemName.setText(surah.name)

    }

    private fun initializeClickListeners() {
        binding.apply {
            searchItemLayout.backItemBtn.setOnClickListener { findNavController().navigateUp() }
            searchItemLayout.searchItemBtn.setOnClickListener {
                showSearchViwAndSearchLogic()
            }
        }
    }

    private fun showSearchViwAndSearchLogic() {
        binding.apply {
            SearchUtils.setupSearchView(
                searchView = searchItemLayout.searchItemView,
                searchButton = searchItemLayout.searchItemBtn,
                title = searchItemLayout.surahItemName,
                backBtn = searchItemLayout.backItemBtn,
                adapter = surahAdapter,
                context = requireContext()
            )
        }
    }

    private fun observeStates() {
        observeErrorState(viewModel.errorState)
        observeLoadingState(viewModel.loadingState)
        observePausingState()
    }

    private fun observeAyahRecitationResponse() {
        viewModel.ayahRecitation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val url = response.data
                    if (url.isNotEmpty()) {
                        // Play the voice from the downloaded file or download it if not present
                        lifecycleScope.launch(Dispatchers.IO){
                            playVoice(url)
                            withContext(Dispatchers.Main){
                                hideProgressBar()
                            }
                        }
                    }
                }

                is NetworkResult.Error -> {
                    viewModel.errorState.value = Pair(true, response.errorMessage)
                }

                is NetworkResult.Loading -> {
                    viewModel.loadingState.value = true
                }
            }
        }
    }

    private suspend fun playVoice(url: String) {
        withContext(Dispatchers.Main) {
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        }
    }


    private fun observePausingState() {
        viewModel.pauseState.observe(viewLifecycleOwner) {
            if (it) {
                pause()
            } else {
                resume()
            }
        }
    }

    private fun pause() {
        exoPlayer?.let {
            if (it.isPlaying) {
                it.playWhenReady = false
            }
        }
    }

    private fun resume() {
        exoPlayer?.let {
            if (!it.isPlaying) {
                it.playWhenReady = true
            }
        }
    }

    private fun initializeRv() {
        binding.ayahRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = surahAdapter
        }
    }


    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun resetStates() {
        viewModel.resetErrorState()
        viewModel.resetLoadingState()
        viewModel.ayahEnded.value = true
    }

    override fun onDestroyView() {
        releaseExoPlayer()
        super.onDestroyView()
    }
}
