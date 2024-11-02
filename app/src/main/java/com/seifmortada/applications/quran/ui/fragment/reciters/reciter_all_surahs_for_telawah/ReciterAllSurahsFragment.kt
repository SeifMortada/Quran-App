package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_all_surahs_for_telawah

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.data.rest.response.reciters.Moshaf
import com.seifmortada.applications.quran.databinding.FragmentReciterMoshafSurahsBinding
import com.seifmortada.applications.quran.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.ui.core.BaseFragment
import com.seifmortada.applications.quran.utils.SearchUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class ReciterAllSurahsFragment : BaseFragment<FragmentReciterMoshafSurahsBinding, Nothing?>() {
    private val args = navArgs<ReciterAllSurahsFragmentArgs>()
    private lateinit var allSurahsAdapter: ReciterAllSurahsAdapter
    private val getQuranUseCase: GetQuranUseCase by inject() // Injecting the use case directly
    private lateinit var moshaf: Moshaf

    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReciterMoshafSurahsBinding {
        return FragmentReciterMoshafSurahsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        moshaf = args.value.moshaf
        initializeDataAndRv(moshaf)

        initializeCLickListeners()


        super.onViewCreated(view, savedInstanceState)
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
                text = moshaf.name
            }
        }
    }

    private fun initializeDataAndRv(moshaf: Moshaf) {
        lifecycleScope.launch(Dispatchers.IO) {
            val surahs = getQuranUseCase()
            allSurahsAdapter = ReciterAllSurahsAdapter(moshaf, surahs)
            withContext(Dispatchers.Main) {
                initializeRv()
            }
        }
    }

    private fun initializeRv() {
        binding.surahsRv.adapter = allSurahsAdapter
    }

    private fun showSearchViwAndSearchLogic() {
        binding.apply {
            SearchUtils.setupSearchView(
                searchView = searchLayout.searchItemView,
                searchButton = searchLayout.searchItemBtn,
                title = searchLayout.surahItemName,
                backBtn = searchLayout.backItemBtn,
                adapter = allSurahsAdapter,
                context = requireContext()
            )
        }
    }
}