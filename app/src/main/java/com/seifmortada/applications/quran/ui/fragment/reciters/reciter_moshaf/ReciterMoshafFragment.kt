package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_moshaf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.seifmortada.applications.quran.databinding.FragmentReciterMoshafBinding
import com.seifmortada.applications.quran.ui.fragment.main.BaseFragment


class ReciterMoshafFragment : BaseFragment<FragmentReciterMoshafBinding>() {
    private val args = navArgs<ReciterMoshafFragmentArgs>()
    private val moshafAdapter=ReciterMoshafAdapter()
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReciterMoshafBinding {
        return FragmentReciterMoshafBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentReciter = args.value.reciterMoshaf
        if (currentReciter.moshaf.isNotEmpty()){
            moshafAdapter.submitList(currentReciter.moshaf)
        }
        initializeRv()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initializeRv() {
        binding.moshafRv.adapter = moshafAdapter
    }
}