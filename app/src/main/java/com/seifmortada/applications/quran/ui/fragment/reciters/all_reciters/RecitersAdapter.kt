package com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemRecitersBinding
import com.seifmortada.applications.quran.data.rest.response.reciters.Reciter
import com.seifmortada.applications.quran.ui.core.BaseRecyclerAdapter
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering

class RecitersAdapter : BaseRecyclerAdapter<Reciter, RecitersAdapter.RecitersViewHolder>() {

    inner class RecitersViewHolder(val binding: ItemRecitersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reciter: Reciter) {
            binding.apply {
                reciterName.text = reciter.name
                moushafNumber.text = reciter.moshaf.size.toString()
            }
            itemView.setOnClickListener {
                it.findNavController().navigate(
                    RecitersFragmentDirections.actionRecitersFragmentToReciterMoshafFragment(
                        reciter
                    )
                )
            }
        }
    }

    override fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): RecitersViewHolder {
        return RecitersViewHolder(
            ItemRecitersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindCustomViewHolder(holder: RecitersViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }

    override fun filterItem(item: Reciter, query: String): Boolean {
        return normalizeTextForFiltering(item.name.lowercase()).contains(query)
    }
}