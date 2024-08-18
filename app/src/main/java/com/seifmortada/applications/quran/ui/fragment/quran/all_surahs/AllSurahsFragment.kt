package com.seifmortada.applications.quran.ui.fragment.quran.all_surahs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.databinding.FragmentAllSurahsBinding
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import com.seifmortada.applications.quran.ui.fragment.main.BaseFragment
import org.koin.android.ext.android.inject


class AllSurahsFragment : BaseFragment<FragmentAllSurahsBinding>() {
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAllSurahsBinding {
        return FragmentAllSurahsBinding.inflate(inflater, container, false)
    }

    private val mainViewModel: MainViewModel by inject()
    private val adapter = AllSurahsAdapter()

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
            updateRecyclerView()
    }



    private fun updateRecyclerView() {
        hideProgressBar()

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


}