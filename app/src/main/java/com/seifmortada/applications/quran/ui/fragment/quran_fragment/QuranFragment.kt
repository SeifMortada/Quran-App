package com.seifmortada.applications.quran.ui.fragment.quran_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.seifmortada.applications.quran.data.api_result.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentQuranBinding
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
import com.seifmortada.applications.quran.domain.model.response.quran.Surah
import com.seifmortada.applications.quran.ui.activity.BaseViewModel
import com.seifmortada.applications.quran.utils.CustomToast
import org.koin.android.ext.android.inject


class QuranFragment : Fragment() {
    private lateinit var binding: FragmentQuranBinding
    private lateinit var viewPager: ViewPager
    private val viewModel: QuranFragmentViewModel by inject()
    private val adapter = SurahsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentQuranBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuranResponse()
        //observeAllSurahs()
    }

    private fun observeQuranResponse() {
        viewModel.quranResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                updateRecyclerView(it.data.surahs.toMutableList())
                Log.e("TAGGGG",it.data.surahs.toString())

            }
        }
    }

    private fun observeAllSurahs() {
        viewModel.allSurahs.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                updateRecyclerView(it.toMutableList())
                Log.e("TAGGGG",it.toString())

            }
        }
    }


    private fun updateRecyclerView(surahs: MutableList<Surah>) {
        adapter.setData(surahs)
        binding.surahsRv.adapter = adapter
        hideProgressBar()

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


}