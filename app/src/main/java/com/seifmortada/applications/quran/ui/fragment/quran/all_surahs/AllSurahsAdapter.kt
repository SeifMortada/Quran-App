package com.seifmortada.applications.quran.ui.fragment.quran.all_surahs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AllSurahsAdapter : RecyclerView.Adapter<AllSurahsAdapter.SurahViewHolder>(),KoinComponent {
    // Inject the ViewModel using Koin
    private val mainViewModel: MainViewModel by inject()
    // Get the list of surahs from the ViewModel
    private var surahs: List<Surah> = mainViewModel.quranData

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
}