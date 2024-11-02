package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_all_telawahs

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.databinding.FragmentReciterMoshafBinding
import com.seifmortada.applications.quran.ui.core.BaseFragment
import com.seifmortada.applications.quran.utils.SearchUtils


class ReciterMoshafFragment : BaseFragment<FragmentReciterMoshafBinding, Nothing?>() {
    private val args = navArgs<ReciterMoshafFragmentArgs>()
    private val moshafAdapter = ReciterMoshafAdapter()
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReciterMoshafBinding {
        return FragmentReciterMoshafBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentReciter = args.value.reciterMoshaf
        if (currentReciter.moshaf.isNotEmpty()) {
            moshafAdapter.submitList(currentReciter.moshaf)
        }
        initializeRv()
        initializeCLickListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeRv() {
        binding.moshafRv.adapter = moshafAdapter
    }

    private fun initializeCLickListeners() {
        binding.apply {
            searchLayout.backItemBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            searchLayout.searchItemBtn.setOnClickListener {
                showSearchViwAndSearchLogic()
            }
            binding.searchLayout.surahItemName.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) // Example size 14sp
                text = "التلاوات"
            }
        }
    }

    private fun showSearchViwAndSearchLogic() {
        binding.apply {
            SearchUtils.setupSearchView(
                searchView = searchLayout.searchItemView,
                searchButton = searchLayout.searchItemBtn,
                title = searchLayout.surahItemName,
                backBtn = searchLayout.backItemBtn,
                adapter = moshafAdapter,
                context = requireContext()
            )
        }
    }
}