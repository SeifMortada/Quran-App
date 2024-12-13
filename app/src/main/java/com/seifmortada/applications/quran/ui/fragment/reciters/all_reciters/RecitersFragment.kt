package com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.domain.model.NetworkResult
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.databinding.FragmentRecitersBinding
import com.seifmortada.applications.quran.ui.core.BaseFragment
import com.seifmortada.applications.quran.ui.custom_views.CustomToast
import com.seifmortada.applications.quran.utils.SearchUtils
import org.koin.android.ext.android.inject


class RecitersFragment : BaseFragment<FragmentRecitersBinding, RecitersViewModel>() {
    private val recitersAdapter = RecitersAdapter()
    override val viewModel: RecitersViewModel by inject()
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecitersBinding {
        return FragmentRecitersBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeClickListeners()
        initializeRv()
        observeReciters()
        initializeViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeReciters() {
        viewModel.recitersResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    recitersAdapter.submitList(it.data)
                    hideProgressBar()
                }

                is NetworkResult.Error -> {
                    CustomToast.makeText(requireContext().applicationContext, it.errorMessage)
                        .show()
                    hideProgressBar()
                }

                is NetworkResult.Loading -> showProgressBar()
                null -> TODO()
            }
        }
    }

    private fun initializeRv() {
        binding.recitersRv.apply {
            adapter = recitersAdapter
        }
    }
     private fun initializeViews(){
        binding.searchLayout.surahItemName.setText(R.string.quran_readers)
    }

    private fun initializeClickListeners() {
        binding.apply {
          searchLayout.backItemBtn.setOnClickListener { findNavController().navigateUp() }
            searchLayout.searchItemBtn.setOnClickListener {
                searchUiAndLogic()
            }
        }
    }

    private fun searchUiAndLogic() {
        binding.apply {
            SearchUtils.setupSearchView(
                searchView =   searchLayout.searchItemView,
                searchButton =   searchLayout.searchItemBtn,
                title = searchLayout.surahItemName,
                backBtn =   searchLayout.backItemBtn,
                adapter = recitersAdapter,
                context = requireContext()
            )
        }

    }
}
