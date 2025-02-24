package com.seifmortada.applications.quran.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.ui.custom_views.CustomToast

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel?> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected open val viewModel: VM? = null

    abstract fun initializeViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initializeViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }

    // Observe loading state LiveData
    protected open fun observeLoadingState(loadingState: LiveData<Boolean>) {
        viewModel?.let {
            loadingState.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) showProgressBar() else hideProgressBar()
            }
        }
    }

    // Observe error state LiveData
    protected open fun observeErrorState(errorState: LiveData<Pair<Boolean, String>>) {
        viewModel?.let {
            errorState.observe(viewLifecycleOwner) { error ->
                if (error.first) {
                    CustomToast.makeText(requireContext().applicationContext, error.second).show()
                    resetStates()
                }
            }
        }
    }

    protected open fun hideProgressBar() {
        binding.root.findViewById<View>(R.id.progressBar)?.visibility = View.INVISIBLE
    }

    protected open fun showProgressBar() {
        binding.root.findViewById<View>(R.id.progressBar)?.visibility = View.VISIBLE
    }

    // To reset states in child fragments
    protected open fun resetStates() {
    }

}
