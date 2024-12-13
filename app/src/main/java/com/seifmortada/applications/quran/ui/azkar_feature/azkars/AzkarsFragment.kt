package com.seifmortada.applications.quran.ui.azkar_feature.azkars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.usecase.GetAzkarsUseCase
import com.seifmortada.applications.quran.databinding.FragmentAzkarsBinding
import com.seifmortada.applications.quran.ui.core.BaseFragment
import com.seifmortada.applications.quran.utils.SearchUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class AzkarsFragment : BaseFragment<FragmentAzkarsBinding,Nothing>() {
    private lateinit var adapter: AzkarsAdapter
    private val getAzkarsUseCase: GetAzkarsUseCase by inject()

    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAzkarsBinding {
        return FragmentAzkarsBinding.inflate(layoutInflater)
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
        binding.searchItemLayout.surahItemName.setText("الأذكار")
    }

    private fun initializeRv() {
        binding.azkarsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AzkarsFragment.adapter
            hideProgressBar()
        }
    }

    private fun initializeAdapter() {
        lifecycleScope.launch(Dispatchers.IO) {
            val azkars = getAzkarsUseCase()
            adapter = AzkarsAdapter()
            adapter.submitList(azkars)
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