package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_moshaf_surahs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.databinding.FragmentReciterMoshafSurahsBinding
import com.seifmortada.applications.quran.ui.fragment.main.BaseFragment
import com.seifmortada.applications.quran.ui.fragment.quran.all_surahs.AllSurahsAdapter
import timber.log.Timber


class ReciterAllSurahsFragment : BaseFragment<FragmentReciterMoshafSurahsBinding,Nothing?>() {
    private val args = navArgs<ReciterAllSurahsFragmentArgs>()
    private lateinit var  allSurahsAdapter : ReciterAllSurahsAdapter

    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReciterMoshafSurahsBinding {
        return FragmentReciterMoshafSurahsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val moshaf=args.value.moshaf
        allSurahsAdapter= ReciterAllSurahsAdapter(moshaf)
        initializeRv()
        binding.availableSurahsTxt.text=" ${moshaf.name}رواية "
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeRv() {
        binding.surahsRv.adapter = allSurahsAdapter
    }
}