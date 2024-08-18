package com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.databinding.ItemRecitersBinding
import com.seifmortada.applications.quran.domain.model.response.reciters.Reciter

class RecitersAdapter : RecyclerView.Adapter<RecitersAdapter.RecitersViewHolder>(), Filterable {

    private var recitersList: List<Reciter> = listOf()
    private var filteredRecitersList: List<Reciter> = listOf()


    inner class RecitersViewHolder(val binding: ItemRecitersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecitersViewHolder {
        return RecitersViewHolder(
            ItemRecitersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int = filteredRecitersList.size

    override fun onBindViewHolder(holder: RecitersViewHolder, position: Int) {
        val currentReciter = filteredRecitersList[position]
        holder.binding.apply {
            reciterName.text = currentReciter.name
            moushafNumber.text = currentReciter.moshaf.size.toString()
        }
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(
                RecitersFragmentDirections.actionRecitersFragmentToReciterMoshafFragment(
                    currentReciter
                )
            )
        }
    }

    fun submitList(reciters: List<Reciter>) {
        this.recitersList = reciters
        filteredRecitersList = reciters
        notifyDataSetChanged()

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim()

                filteredRecitersList = if (query.isNullOrEmpty()) {
                    recitersList // If the query is empty, show the full list
                } else {
                    recitersList.filter {
                        it.name.lowercase().contains(query)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredRecitersList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredRecitersList = results?.values as List<Reciter>
                notifyDataSetChanged()
            }
        }
    }
}