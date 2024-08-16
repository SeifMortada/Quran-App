package com.seifmortada.applications.quran.ui.fragment.all_surahs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.data.model.quran.Surah

class AllSurahsAdapter : RecyclerView.Adapter<AllSurahsAdapter.SurahViewHolder>() {
    private var surahs: MutableList<Surah> = mutableListOf()

    inner class SurahViewHolder(val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(surah: Surah) {
            binding.surahName.text = surah.name
            val verses = surah.total_verses.toString()
            binding.surahNozol.text =
                if (surah.type == "meccan") "مكية -$verses" + " آيه" else "مدنية -$verses" +" آيه"
            binding.surahNumber.text = surah.id.toString()

            binding.root.setOnClickListener {
                it.findNavController()
                    .navigate(AllSurahsFragmentDirections.actionQuranFragmentToSurahFragment(surah))
            }
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