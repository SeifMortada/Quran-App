package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_all_surahs_for_telawah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.MoshafModel
import com.example.domain.model.SurahModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.ui.core.BaseRecyclerAdapter
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering

class ReciterAllSurahsAdapter (private val moshaf: MoshafModel, private val surahs: List<SurahModel>):
    BaseRecyclerAdapter<SurahModel, ReciterAllSurahsAdapter.SurahViewHolder>(
      //filtering the surahs to show only the surahs that has this telawah for the reciter
        surahs.filter { it.id in moshaf.surahList.split(",").map { it.toInt() } })
{
    inner class SurahViewHolder(val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(surah: SurahModel) {
            binding.surahName.text = surah.name
            val verses = surah.totalVerses.toString()
            binding.surahNozol.text =
                if (surah.type == "meccan") "مكية -$verses" + " آيه" else "مدنية -$verses" + " آيه"
            binding.surahNumber.text = surah.id.toString()
            itemView.setOnClickListener {
                it.findNavController().navigate(
                    ReciterAllSurahsFragmentDirections.actionReciterAllSurahsFragmentToSurahRecitationFragment(
                        SurahMoshafReciter(moshaf, surah.id)
                    )
                )
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
        holder.bind(filteredItems[position])
    }

    override fun filterItem(item: SurahModel, query: String): Boolean {
        return normalizeTextForFiltering(item.name.lowercase()).contains(query)
    }
}