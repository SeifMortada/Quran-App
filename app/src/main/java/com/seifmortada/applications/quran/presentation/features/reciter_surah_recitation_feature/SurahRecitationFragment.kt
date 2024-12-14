package com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.model.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentSurahRecitationBinding
import com.seifmortada.applications.quran.presentation.core.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class SurahRecitationFragment :
    BaseFragment<FragmentSurahRecitationBinding, SurahRecitationViewModel?>() {

    private val args = navArgs<SurahRecitationFragmentArgs>()

    override val viewModel: SurahRecitationViewModel by inject()

    private lateinit var surahRecitationsAdapter: SurahRecitationAdapter

    private var exoPlayer: ExoPlayer? = null

    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSurahRecitationBinding {
        return FragmentSurahRecitationBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val moshaf = args.value.surahMoshaf.moshaf
        val surahId = args.value.surahMoshaf.surahId
        val server = moshaf.server
        viewModel.fetchRecitation(server, surahId)
        observeSurahResponse()
        observeErrorState(viewModel.errorState)
        observeLoadingState(viewModel.loadingState)
        initializeClickListeners()
        initializeExoPlayer()
        observeSurahRecitationResponse()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeSurahResponse(){
        viewModel.surahByIdResponse.observe(viewLifecycleOwner){
            if (it!=null)
                surahRecitationsAdapter=SurahRecitationAdapter(it)
        }
    }
    private fun initializeExoPlayer() {
        exoPlayer =
            ExoPlayer.Builder(requireContext()).build().apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            showPlayButton()
                        }
                    }
                })
            }
    }

    private fun observeSurahRecitationResponse() {
        viewModel.surahRecitationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val url = it.data
                    if (url.isNotEmpty()) {
                        // Play the voice from the downloaded file or download it if not present
                        CoroutineScope(Dispatchers.IO).launch {
                            playVoice(url)
                        }
                        showPauseButton()
                        hideProgressBar()
                    }
                }

                is NetworkResult.Error -> {
                    viewModel.errorState.value = Pair(true, it.errorMessage)
                }

                is NetworkResult.Loading -> viewModel.loadingState.value = true
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

    private fun initializeClickListeners() {
        binding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            searchBtn.setOnClickListener {
                showSearchViwAndSearchLogic()
            }
        }
    }
    private fun showPlayButton(){
        binding.apply {
            playBtn.isEnabled=true
            playBtn.visibility=View.VISIBLE
            pauseBtn.isEnabled=false
            pauseBtn.visibility=View.INVISIBLE
        }
    }
    private fun showPauseButton(){
        binding.apply {
            playBtn.isEnabled=false
            playBtn.visibility=View.INVISIBLE
            pauseBtn.isEnabled=true
            pauseBtn.visibility=View.VISIBLE
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
                    surahRecitationsAdapter.filter.filter(newText)
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
    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
    override fun resetStates() {
        viewModel.resetErrorState()
        viewModel.resetLoadingState()
        super.resetStates()
    }

    override fun onDestroyView() {
        releaseExoPlayer()
        super.onDestroyView()
    }
}