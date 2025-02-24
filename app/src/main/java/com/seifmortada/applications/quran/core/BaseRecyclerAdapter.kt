package com.seifmortada.applications.quran.core

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
    private var items: List<T> = emptyList()
) : RecyclerView.Adapter<VH>() {

     var filteredItems: List<T> = items

    abstract fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun onBindCustomViewHolder(holder: VH, position: Int)

    open fun filterItem(item: T, query: String): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateCustomViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindCustomViewHolder(holder, position)
    }

    fun submitList(newItems: List<T>) {
        val diffCallback = BaseDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        filteredItems = newItems

        diffResult.dispatchUpdatesTo(this)
    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter { filterItem(it, query) }
        }
        notifyDataSetChanged()
    }

    class BaseDiffCallback<T>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

