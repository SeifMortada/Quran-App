package com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.navigation.fragment.findNavController
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.databinding.FragmentRecitersBinding
import com.seifmortada.applications.quran.ui.fragment.main.BaseFragment
import com.seifmortada.applications.quran.utils.CustomToast
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

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeReciters() {
        viewModel.recitersResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    recitersAdapter.submitList(it.data.reciters)
                    hideProgressBar()
                }

                is NetworkResult.Error -> {
                    CustomToast.makeText(requireContext().applicationContext, it.errorMessage)
                        .show()
                    hideProgressBar()
                }

                is NetworkResult.Loading -> showProgressBar()
            }
        }
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
            searchBtn.visibility = View.INVISIBLE
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
