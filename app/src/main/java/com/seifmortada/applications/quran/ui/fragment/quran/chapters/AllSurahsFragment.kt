package com.seifmortada.applications.quran.ui.fragment.quran.chapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.databinding.FragmentAllSurahsBinding
import com.seifmortada.applications.quran.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.ui.core.BaseFragment
import com.seifmortada.applications.quran.utils.SearchUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class AllSurahsFragment : BaseFragment<FragmentAllSurahsBinding, Nothing?>() {
    private val getQuranUseCase: GetQuranUseCase by inject()
    private lateinit var adapter: AllSurahsAdapter
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAllSurahsBinding {
        return FragmentAllSurahsBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        clickListeners()
        initializeAdapter()
    }

    private fun clickListeners() {
        binding.searchItemLayout.searchItemBtn.setOnClickListener {
            searchUiAndLogic()
        }
        binding.searchItemLayout.backItemBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.searchItemLayout.surahItemName.setText(R.string.quran)
    }

    private fun initializeRv() {
        binding.surahsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllSurahsFragment.adapter
            hideProgressBar()
        }
    }

    private fun initializeAdapter() {
        lifecycleScope.launch(Dispatchers.IO) {
            val surahs = getQuranUseCase()
            adapter = AllSurahsAdapter()
            adapter.submitList(surahs)
            withContext(Dispatchers.Main) {
                initializeRv()
            }
        }
    }

    private fun searchUiAndLogic() {
        binding.apply {
                SearchUtils.setupSearchView(
                    searchView =searchItemLayout.searchItemView,
                    searchButton = searchItemLayout.searchItemBtn,
                    title = searchItemLayout.surahItemName,
                    backBtn = searchItemLayout.backItemBtn,
                    adapter = adapter,
                    context = requireContext()
                )
        }

    }
}