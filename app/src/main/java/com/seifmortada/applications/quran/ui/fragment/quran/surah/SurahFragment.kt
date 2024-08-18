package com.seifmortada.applications.quran.ui.fragment.quran.surah

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentSurahBinding
import com.seifmortada.applications.quran.ui.fragment.main.BaseFragment
import com.seifmortada.applications.quran.utils.CustomToast
import org.koin.android.ext.android.inject
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SurahFragment : BaseFragment<FragmentSurahBinding>() {

    private val args by navArgs<SurahFragmentArgs>()
    private val surahViewModel: SurahViewModel by inject()
    private val surahAdapter = SurahAdapter()
    private var exoPlayer: ExoPlayer? = null

    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSurahBinding {
        return FragmentSurahBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeAdapter()
        initializeRv()
        initializeClickListeners()
        observeStates()
        submitList()
        observeAyahRecitationResponse()

        // Initialize ExoPlayer
        exoPlayer =
            ExoPlayer.Builder(requireContext()).build().apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            surahViewModel.ayahEnded.value = true
                        }
                    }
                })
            }
        surahViewModel.exoPlayer = exoPlayer!!

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeAdapter() {
        surahAdapter.apply {
            viewModel = surahViewModel
            currentSurah = args.surah
            lifecycleOwner = viewLifecycleOwner
        }
    }
    private fun initializeClickListeners() {
        binding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            searchBtn.setOnClickListener {
                showSearchViwAndSearchLogic()
            }
        }
    }
private fun showSearchViwAndSearchLogic() {
    binding.apply {
            searchBtn.visibility = View.INVISIBLE
            searchView.visibility = View.VISIBLE
            searchView.isIconified = false // Ensure  the search view is not collapsed
            searchView.requestFocus() // Request focus to show the keyboard

            // Show the keyboard manually
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT)


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    searchView.visibility = View.INVISIBLE
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    surahAdapter.filter.filter(newText)
                    // Hide the SearchView if the text is cleared
                    if (newText.isNullOrEmpty()) {
                        searchView.visibility = View.GONE
                        searchBtn.visibility = View.VISIBLE
                    }
                    return true
                }
            })
            searchView.setOnCloseListener {
                searchView.visibility = View.GONE
                searchBtn.visibility = View.VISIBLE
                false
            }

    }
}
    private fun observeStates() {
        observeErrorState()
        observeLoadingState()
        observePausingState()
    }

    private fun observeAyahRecitationResponse() {
        surahViewModel.ayahRecitation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val url = response.data
                    if (url.isNotEmpty()) {
                        // Play the voice from the downloaded file or download it if not present
                        CoroutineScope(Dispatchers.IO).launch {
                            playVoice(url)
                        }
                        surahViewModel.resetLoadingState()
                    }
                }

                is NetworkResult.Error -> {
                    surahViewModel.errorState.value = Pair(true, response.errorMessage)
                }

                is NetworkResult.Loading -> {
                    surahViewModel.loadingState.value = true
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
        surahViewModel.pauseState.observe(viewLifecycleOwner) {
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

    private fun submitList() {
        val ayahs = args.surah.verses
        binding.surahName.text=args.surah.name
        if (ayahs.isNotEmpty())
            surahAdapter.submitList(ayahs)
    }

    private fun initializeRv() {
        binding.ayahRv.apply {
            adapter = surahAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLoadingState() {
        surahViewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) showProgressBar() else hideProgressBar()
        }
    }

    private fun observeErrorState() {
        surahViewModel.errorState.observe(viewLifecycleOwner) {
            if (it.first) {
                CustomToast.makeText(requireContext().applicationContext, it.second).show()
                surahViewModel.resetErrorState()
                surahViewModel.resetLoadingState()
                surahViewModel.ayahEnded.value=true
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onDestroyView() {
        releaseExoPlayer()
        super.onDestroyView()
    }
}
