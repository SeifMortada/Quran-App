package com.seifmortada.applications.quran.ui.fragment.surah

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.data.model.quran.Surah
import com.seifmortada.applications.quran.data.model.quran.Verse
import com.seifmortada.applications.quran.databinding.ItemAyahBinding

class SurahAdapter : RecyclerView.Adapter<SurahAdapter.AyahViewHolder>() {

    var ayahs = listOf<Verse>()
    lateinit var viewModel: SurahViewModel
    lateinit var currentSurah: Surah

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

    override fun getItemCount(): Int = ayahs.size

    override fun onBindViewHolder(holder: AyahViewHolder, position: Int) {
        val currentAyah = ayahs[position]
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
                viewModel.getAyahRecitation(currentSurah.id.toString(), currentAyah.id.toString())
            }

        }
    }

    fun submitList(verses: List<Verse>) {
        this.ayahs = verses
        notifyDataSetChanged()
    }
}