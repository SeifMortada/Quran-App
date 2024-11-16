package com.seifmortada.applications.quran.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.domain.model.main.MainItem
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.databinding.FragmentMainBinding
import com.seifmortada.applications.quran.ui.core.BaseFragment


class MainFragment : BaseFragment<FragmentMainBinding, Nothing?>() {

    private lateinit var mainAdapter: MainAdapter
    override fun initializeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeMainItems()

        binding.mainRv.apply {
            adapter = mainAdapter
            setHasFixedSize(true)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeMainItems() {
        mainAdapter = MainAdapter()
        val list = listOf<MainItem>(
            MainItem(getString(R.string.zikrs), R.drawable.ic_tasbih),
            MainItem(getString(R.string.quran), R.drawable.ic_koran),
            MainItem(getString(R.string.quran_readers), R.drawable.ic_imam)
        )
        mainAdapter.setItems(list)

    }

}