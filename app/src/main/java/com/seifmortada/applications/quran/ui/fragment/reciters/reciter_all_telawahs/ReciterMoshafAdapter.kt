package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_all_telawahs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.MoshafModel
import com.seifmortada.applications.quran.databinding.ItemReciterMoshafBinding
import com.seifmortada.applications.quran.ui.core.BaseRecyclerAdapter
import com.seifmortada.applications.quran.utils.FunctionsUtils

class ReciterMoshafAdapter :
    BaseRecyclerAdapter<MoshafModel, ReciterMoshafAdapter.ReciterMoshafViewHolder>() {

    inner class ReciterMoshafViewHolder(val binding: ItemReciterMoshafBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(moshaf: MoshafModel) {
            binding.moshafName.text = moshaf.name
            itemView.setOnClickListener {
                it.findNavController().navigate(
                    ReciterMoshafFragmentDirections.actionReciterMoshafFragmentToReciterAllSurahsFragment(
                        moshaf
                    )
                )
            }
        }
    }

    override fun onCreateCustomViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReciterMoshafViewHolder {
        return ReciterMoshafViewHolder(
            ItemReciterMoshafBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindCustomViewHolder(holder: ReciterMoshafViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }

    override fun filterItem(item: MoshafModel, query: String): Boolean {
        return FunctionsUtils.normalizeTextForFiltering(item.name.lowercase()).contains(query)
    }
}