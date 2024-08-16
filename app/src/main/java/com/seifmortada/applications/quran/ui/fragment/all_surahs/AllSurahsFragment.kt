package com.seifmortada.applications.quran.ui.fragment.all_surahs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.databinding.FragmentAllSurahsBinding
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import org.koin.android.ext.android.inject


class AllSurahsFragment : Fragment() {
    private lateinit var binding: FragmentAllSurahsBinding
    private val mainViewModel: MainViewModel by inject()
    private val adapter = AllSurahsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllSurahsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        initializeRv()
        observeAllSurahs()
    }

    private fun initializeRv() {
        binding.surahsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllSurahsFragment.adapter // Avoid reassigning adapter to `this.adapter`
        }
    }

    private fun observeAllSurahs() {
        val surahList = mainViewModel.quranData
        if (surahList.isNotEmpty()) {
            updateRecyclerView(surahList.toMutableList())
        }
    }



    private fun updateRecyclerView(surahs: MutableList<Surah>) {
        adapter.setData(surahs)
        hideProgressBar()

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


}