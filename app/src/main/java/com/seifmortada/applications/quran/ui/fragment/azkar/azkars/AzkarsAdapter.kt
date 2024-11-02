package com.seifmortada.applications.quran.ui.fragment.azkar.azkars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.data.local.room.entities.azkar.AzkarItem
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.databinding.ItemAzkarsBinding
import com.seifmortada.applications.quran.ui.core.BaseRecyclerAdapter
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering

class AzkarsAdapter()
    : BaseRecyclerAdapter<AzkarItem, AzkarsAdapter.AzkarsViewHolder>() {

    inner class AzkarsViewHolder(val binding: ItemAzkarsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(zikr: AzkarItem) {
            binding.zikrName.text = zikr.category
            binding.zikrNumber.text = zikr.id.toString()
        }
    }

    override fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): AzkarsViewHolder {
        return AzkarsViewHolder(
            ItemAzkarsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindCustomViewHolder(holder: AzkarsViewHolder, position: Int) {
        val currentZirk = filteredItems[position]
        holder.bind(currentZirk)    }


    override fun filterItem(item: AzkarItem, query: String): Boolean {
        return normalizeTextForFiltering(item.category.lowercase()).contains(query)
    }
}