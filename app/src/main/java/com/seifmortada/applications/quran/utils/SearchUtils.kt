package com.seifmortada.applications.quran.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.presentation.core.BaseRecyclerAdapter

object SearchUtils {

    fun <T, VH : RecyclerView.ViewHolder> setupSearchView(
        searchView: SearchView,
        searchButton: View,
        title:TextView,
        backBtn:ImageView,
        adapter: BaseRecyclerAdapter<T, VH>,  // Accept the generic BaseRecyclerAdapter
        context: Context
    ) {
        searchView.visibility = View.VISIBLE
        searchButton.visibility = View.GONE
        title.visibility=View.GONE
        backBtn.visibility=View.GONE
        searchView.isIconified = false
        searchView.requestFocus()

        // Show the keyboard manually
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT)

        // Set up the search view listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.visibility = View.GONE
                searchButton.visibility = View.VISIBLE
                title.visibility=View.VISIBLE
                backBtn.visibility=View.VISIBLE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")  // Call the adapter's filter method
                if (newText.isNullOrEmpty()) {
                    searchView.visibility = View.GONE
                    searchButton.visibility = View.VISIBLE
                    title.visibility=View.VISIBLE
                    backBtn.visibility=View.VISIBLE
                }
                return true
            }
        })

        // Set up the close listener
        searchView.setOnCloseListener {
            searchView.visibility = View.GONE
            searchButton.visibility = View.VISIBLE
            title.visibility=View.VISIBLE
            backBtn.visibility=View.VISIBLE
            false
        }
    }
}
