package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_moshaf_surahs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.domain.model.response.reciters.Moshaf
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class ReciterAllSurahsAdapter(val moshaf: Moshaf) : RecyclerView.Adapter<ReciterAllSurahsAdapter.SurahViewHolder>(),
    KoinComponent {
    // Inject the ViewModel using Koin
    private val mainViewModel: MainViewModel by inject()
    private val availableSurahIds = moshaf.surahList.map { it.code } // Convert the list of strings to integers
    // Get the list of surahs from the ViewModel
    private var surahs: List<Surah> = mainViewModel.quranData

    private val filteredSurahs = surahs.filter { it.id in availableSurahIds } // Filter surahs based on IDs

    inner class SurahViewHolder(val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(surah: Surah) {
            binding.surahName.text = surah.name
            val verses = surah.total_verses.toString()
            binding.surahNozol.text =
                if (surah.type == "meccan") "مكية -$verses" + " آيه" else "مدنية -$verses" + " آيه"
            binding.surahNumber.text = surah.id.toString()

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

    override fun getItemCount(): Int = filteredSurahs.size

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        Timber.d("Surahs Size Before: ${surahs.size}")
        val currentSurah = filteredSurahs[position]
        holder.bind(currentSurah)
    }

    // Function to filter surahs based on the available surahs in the Moshaf object
  /*  fun filterList(moshaf: Moshaf) {
        val availableSurahIds = moshaf.surahList.map { it.code } // Convert the list of strings to integers
        val filteredSurahs = surahs.filter { it.id in availableSurahIds } // Filter surahs based on IDs
        updateData(filteredSurahs)
    }

    // Update the adapter data and notify the change
    private fun updateData(newSurahs: List<Surah>) {
        surahs = newSurahs
        notifyDataSetChanged()
    }*/
}