package com.seifmortada.applications.quran.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.data.model.main.MainItem
import com.seifmortada.applications.quran.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mainAdapter:MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)
        mainAdapter = MainAdapter(requireContext())
        val list= listOf<MainItem>(
            MainItem(getString(R.string.zikrs),R.drawable.ic_tasbih),
            MainItem(getString(R.string.quran),R.drawable.ic_koran),
            MainItem(getString(R.string.quran_readers),R.drawable.ic_imam)
        )
        mainAdapter.setItems(list)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mainRv.apply {
            adapter = mainAdapter
            setHasFixedSize(true)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}