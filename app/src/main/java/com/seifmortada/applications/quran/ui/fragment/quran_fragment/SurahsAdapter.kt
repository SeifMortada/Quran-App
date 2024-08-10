package com.seifmortada.applications.quran.ui.fragment.quran_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.domain.model.response.quran.Surah

class SurahsAdapter : RecyclerView.Adapter<SurahsAdapter.SurahViewHolder>() {
    private var surahs: MutableList<Surah> = mutableListOf()

    inner class SurahViewHolder(val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(surah: Surah) {
            binding.surahName.text = surah.name
            binding.surahNzol.text = surah.revelationType
            binding.surahVerses.text = surah.ayahs.size.toString()
            binding.surahName.text = surah.number.toString()

        }
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

    override fun getItemCount(): Int = surahs.size

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val currentSurah = surahs[position]
        holder.bind(currentSurah)
    }

    fun setData(pages: MutableList<Surah>) {
        this.surahs = pages
        notifyDataSetChanged()
    }
}