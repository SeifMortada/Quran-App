package com.seifmortada.applications.quran.ui.fragment.reciters.surah_telawah

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.SurahModel
import com.example.domain.model.VerseModel
import com.seifmortada.applications.quran.databinding.ItemAyahReciterBinding
import java.text.Normalizer
import java.util.regex.Pattern

class SurahRecitationAdapter(private val surah: SurahModel) :
    RecyclerView.Adapter<SurahRecitationAdapter.AyahViewHolder>(), Filterable {

    private var ayahs: List<VerseModel> = surah.verses
    private var filteredAyahList: List<VerseModel> = surah.verses


    inner class AyahViewHolder(val binding: ItemAyahReciterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        return AyahViewHolder(
            ItemAyahReciterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = filteredAyahList.size

    override fun onBindViewHolder(holder: AyahViewHolder, position: Int) {
        val currentAyah = filteredAyahList[position]
        holder.binding.apply {
            ayah.text = currentAyah.text
            ayahNumber.text = currentAyah.id.toString()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim()

                filteredAyahList = if (query.isNullOrEmpty()) {
                    ayahs // If the query is empty, show the full list
                } else {
                    ayahs.filter {
                        normalize(it.text.lowercase()).contains(query)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredAyahList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredAyahList = results?.values as List<VerseModel>
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Normalizes the Arabic text by removing diacritics (tashkeel) and other symbols.
     */
    private fun normalize(text: String): String {
        val nfdNormalizedString = Normalizer.normalize(text, Normalizer.Form.NFD)
        // Remove diacritical marks (tashkeel)
        val pattern = Pattern.compile("\\p{M}")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }
}