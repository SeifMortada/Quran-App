package com.seifmortada.applications.quran.features.quran_chapters_feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.SurahModel
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.core.BaseRecyclerAdapter
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering

class AllSurahsAdapter()
    : BaseRecyclerAdapter<SurahModel, AllSurahsAdapter.SurahViewHolder>() {

    inner class SurahViewHolder(val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(surah: SurahModel) {
            binding.surahName.text = surah.name
            val verses = surah.totalVerses.toString()
            binding.surahNozol.text =
                if (surah.type == "meccan") "مكية -$verses" + " آيه" else "مدنية -$verses" + " آيه"
            binding.surahNumber.text = surah.id.toString()

            binding.root.setOnClickListener {
                it.findNavController()
                    .navigate(AllSurahsFragmentDirections.actionQuranFragmentToSurahFragment(surah.id))
            }
        }
    }

    override fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        return SurahViewHolder(
            ItemSurahBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindCustomViewHolder(holder: SurahViewHolder, position: Int) {
        val currentSurah = filteredItems[position]
        holder.bind(currentSurah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        return SurahViewHolder(
            ItemSurahBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun filterItem(item: SurahModel, query: String): Boolean {
        return normalizeTextForFiltering(item.name.lowercase()).contains(query)
    }
}