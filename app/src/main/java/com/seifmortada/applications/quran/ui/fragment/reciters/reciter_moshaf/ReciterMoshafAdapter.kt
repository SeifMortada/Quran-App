package com.seifmortada.applications.quran.ui.fragment.reciters.reciter_moshaf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemReciterMoshafBinding
import com.seifmortada.applications.quran.domain.model.response.reciters.Moshaf

class ReciterMoshafAdapter : RecyclerView.Adapter<ReciterMoshafAdapter.ReciterMoshafViewHolder>() {
    private var reciterMoshaf: List<Moshaf> = emptyList()

    inner class ReciterMoshafViewHolder(val binding: ItemReciterMoshafBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReciterMoshafViewHolder {
        return ReciterMoshafViewHolder(
            ItemReciterMoshafBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = reciterMoshaf.size

    override fun onBindViewHolder(holder: ReciterMoshafViewHolder, position: Int) {
        val currentMoshaf = reciterMoshaf[position]
        holder.binding.apply {
            moshafName.text = currentMoshaf.name
        }
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(ReciterMoshafFragmentDirections.actionReciterMoshafFragmentToReciterAllSurahsFragment(currentMoshaf))
        }
    }

    fun submitList(moshaf: List<Moshaf>) {
        this.reciterMoshaf = moshaf
    }
}