package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_moshaf_surahs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemSurahBinding
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.data.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.domain.model.response.reciters.Moshaf
import com.seifmortada.applications.quran.ui.activity.MainViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class ReciterAllSurahsAdapter(private val moshaf: Moshaf) :
    RecyclerView.Adapter<ReciterAllSurahsAdapter.SurahViewHolder>(),
    KoinComponent {
    // Inject the ViewModel using Koin
    private val mainViewModel: MainViewModel by inject()

    private val numbersInMoshaf = moshaf.surahList.split(",") // Get the numbers from the surah List

    private val availableSurahIds =
        numbersInMoshaf.map { it.toInt() } // Convert the list of strings to integers


    private var surahs: List<Surah> =
        mainViewModel.quranData   // Get the list of surahs from the ViewModel

    private val filteredSurahs =
        surahs.filter { it.id in availableSurahIds } // Filter surahs based on IDs

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
        val currentSurah = filteredSurahs[position]
        holder.bind(currentSurah)
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(
                ReciterAllSurahsFragmentDirections.actionReciterAllSurahsFragmentToSurahRecitationFragment(
                    SurahMoshafReciter(moshaf, currentSurah)
                )
            )
        }
    }
}