package com.seifmortada.applications.quran.features.surah_feature

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.SurahModel
import com.example.domain.model.VerseModel
import com.seifmortada.applications.quran.databinding.ItemAyahBinding
import com.seifmortada.applications.quran.core.BaseRecyclerAdapter
import com.seifmortada.applications.quran.utils.FunctionsUtils.normalizeTextForFiltering

class SurahAdapter(
    private val surahViewModel: SurahViewModel,
    private val currentSurah: SurahModel,
    private val lifecycleOwner: LifecycleOwner
) : BaseRecyclerAdapter<VerseModel, SurahAdapter.AyahViewHolder>() {

    private var paused: Boolean = false

    inner class AyahViewHolder(val binding: ItemAyahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VerseModel) {
            binding.apply {
                ayah.text = item.text
                ayahNumber.text = item.id.toString()

                shareBtn.setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "${item.text} (${item.id})")
                    }
                    it.context.startActivity(Intent.createChooser(shareIntent, "Share Ayah"))
                }

                playBtn.setOnClickListener {
                    showPauseButton()
                    if (paused) {
                        surahViewModel.resumeAyahRecitation()
                        paused = false
                    } else {
                        surahViewModel.getAyahRecitation(
                            currentSurah.id.toString(),
                            item.id.toString()
                        )
                    }
                }

                pauseBtn.setOnClickListener {
                    surahViewModel.pauseAyahRecitation()
                    showPlayButton()
                    paused = true
                }

                surahViewModel.ayahEnded.observe(lifecycleOwner) {
                    if (it) showPlayButton()
                }
            }
        }


        private fun showPlayButton() {
            binding.apply {
                playBtn.isEnabled = true
                playBtn.visibility = View.VISIBLE
                pauseBtn.isEnabled = false
                pauseBtn.visibility = View.INVISIBLE
            }
        }

        private fun showPauseButton() {
            binding.apply {
                playBtn.isEnabled = false
                playBtn.visibility = View.INVISIBLE
                pauseBtn.isEnabled = true
                pauseBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        return AyahViewHolder(
            ItemAyahBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindCustomViewHolder(holder: AyahViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }

    override fun filterItem(item: VerseModel, query: String): Boolean {
        return normalizeTextForFiltering(item.text.lowercase()).contains(query)
    }
}
