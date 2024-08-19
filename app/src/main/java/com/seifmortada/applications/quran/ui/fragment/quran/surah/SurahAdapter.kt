package com.seifmortada.applications.quran.ui.fragment.quran.surah

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.data.model.quran.Verse
import com.seifmortada.applications.quran.databinding.ItemAyahBinding
import com.seifmortada.applications.quran.domain.model.response.reciters.Reciter
import java.text.Normalizer
import java.util.regex.Pattern

class SurahAdapter : RecyclerView.Adapter<SurahAdapter.AyahViewHolder>() ,Filterable {

    private var ayahs: List<Verse> = listOf()
    private var filteredAyahList: List<Verse> = listOf()

    lateinit var surahviewModel: SurahViewModel
    lateinit var currentSurah: Surah
    lateinit var lifecycleOwner: LifecycleOwner
    private var pasued = false

    inner class AyahViewHolder(val binding: ItemAyahBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        return AyahViewHolder(
            ItemAyahBinding.inflate(
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
            // Set up the share button click listener
            shareBtn.setOnClickListener {
                // Create an intent to share the verse
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${currentAyah.text} (${currentAyah.id})")
                }
                // Start the sharing activity
                it.context.startActivity(Intent.createChooser(shareIntent, "الآايه إالي نشر "))
            }
            playBtn.setOnClickListener {
                showPauseButton(this)
                if (pasued) {
                    surahviewModel.resumeAyahRecitation()
                    pasued = false
                } else {
                    surahviewModel.getAyahRecitation(
                        currentSurah.id.toString(),
                        currentAyah.id.toString()
                    )
                }
            }
            pauseBtn.setOnClickListener {
                surahviewModel.pauseAyahRecitation()
                showPlayButton(this)
                pasued = true

            }
            surahviewModel.ayahEnded.observe(lifecycleOwner) {
                if (it) {
                    showPlayButton(this)
                }
            }
        }
    }

    private fun showPlayButton(itemAyahBinding: ItemAyahBinding) {
        itemAyahBinding.apply {
            playBtn.isEnabled = true
            playBtn.visibility = View.VISIBLE
            pauseBtn.isEnabled = false
            pauseBtn.visibility = View.INVISIBLE
        }
    }

    private fun showPauseButton(itemAyahBinding: ItemAyahBinding) {
        itemAyahBinding.apply {
            playBtn.isEnabled = false
            playBtn.visibility = View.INVISIBLE
            pauseBtn.isEnabled = true
            pauseBtn.visibility = View.VISIBLE
        }
    }

    fun submitList(verses: List<Verse>) {
        this.ayahs = verses
        filteredAyahList = verses
        notifyDataSetChanged()
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
                filteredAyahList = results?.values as List<Verse>
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