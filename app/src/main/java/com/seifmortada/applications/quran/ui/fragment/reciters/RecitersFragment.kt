package com.seifmortada.applications.quran.ui.fragment.reciters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentRecitersBinding
import com.seifmortada.applications.quran.domain.model.response.reciters.Reciter
import com.seifmortada.applications.quran.utils.CustomToast
import org.koin.android.ext.android.inject


class RecitersFragment : Fragment() {
    private lateinit var binding: FragmentRecitersBinding
    private val recitersAdapter = RecitersAdapter()
    private val recitersViewMode: RecitersViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecitersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeClickListeners()
        initializeRv()
        observeReciters()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeReciters() {
        recitersViewMode.recitersResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    recitersAdapter.submitList(it.data.reciters)
                    hideProgressBar()
                }

                is NetworkResult.Error -> {
                    hideProgressBar()
                    CustomToast.makeText(requireContext(), it.errorMessage)
                }

                is NetworkResult.Loading -> showProgressBar()
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun initializeRv() {
        binding.recitersRv.apply {
            adapter = recitersAdapter
        }
    }

    private fun initializeClickListeners() {
        binding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            searchBtn.setOnClickListener {
                showSearchViewAndSearchLogic()
            }
        }
    }

    private fun showSearchViewAndSearchLogic() {
        binding.apply {
            searchView.visibility = View.VISIBLE
            searchBtn.visibility=View.INVISIBLE
            searchView.isIconified = false // Ensure the search view is not collapsed
            searchView.requestFocus() // Request focus to show the keyboard

            // Show the keyboard manually
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT)


            searchView.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    searchView.visibility = View.INVISIBLE
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    recitersAdapter.filter.filter(newText)
                    // Hide the SearchView if the text is cleared
                    if (newText.isNullOrEmpty()) {
                        searchView.visibility = View.GONE
                        searchBtn.visibility = View.VISIBLE
                    }
                    return true
                }
            })
            searchView.setOnCloseListener {
                searchView.visibility = View.GONE
                searchBtn.visibility = View.VISIBLE
                false
            }
        }
    }

}
