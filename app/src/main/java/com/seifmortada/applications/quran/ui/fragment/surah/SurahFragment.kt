package com.seifmortada.applications.quran.ui.fragment.surah

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentSurahBinding
import com.seifmortada.applications.quran.utils.CustomToast
import org.koin.android.ext.android.inject


class SurahFragment : Fragment() {
    private val args = navArgs<SurahFragmentArgs>()
    private lateinit var binding: FragmentSurahBinding
    private val surahViewModel: SurahViewModel by inject()
    private val surahAdapter = SurahAdapter()
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurahBinding.inflate(inflater)
        surahAdapter.viewModel = surahViewModel
        surahAdapter.currentSurah = args.value.surah
        observeErrorState()
        observeLoadingState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeRv()
        submitList()
        observeAyahRecitationResponse()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun playVoice(url: String) {
        mediaPlayer = MediaPlayer().apply {
            reset()
            setDataSource(url) // Use the URL directly
            prepareAsync()
            setOnPreparedListener {
                start()
            }
        }
    }

    private fun observeAyahRecitationResponse() {
        surahViewModel.ayahRecitation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val url = response.data
                    if (url.isNotEmpty()) {
                        playVoice(url)
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


    private fun submitList() {
        val ayahs = args.value.surah.verses
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
            if (it.first)
                CustomToast.makeText(requireContext().applicationContext, it.second).show()
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }


    override fun onDestroyView() {
        releaseMediaPlayer()
        super.onDestroyView()
    }
}