package com.seifmortada.applications.quran.ui.fragment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.data.model.main.MainItem
import com.seifmortada.applications.quran.databinding.ItemMainBinding
import com.seifmortada.applications.quran.utils.FunctionsUtils

class MainAdapter :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val items: MutableList<MainItem> = mutableListOf()

    inner class MainViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = items[position]
        holder.binding.apply {
            titleTxt.text = currentItem.title
            imageView.setImageResource(currentItem.image)
        }
        when (holder.position) {
            0 -> holder.itemView.setOnClickListener {
                holder.itemView.findNavController()
                    .navigate(R.id.action_mainFragment_to_azkarsFragment)}
            1 -> holder.itemView.setOnClickListener {
                holder.itemView.findNavController()
                    .navigate(R.id.action_mainFragment_to_quranFragment)
            }

            2 -> holder.itemView.setOnClickListener {
                holder.itemView.findNavController()
                    .navigate(R.id.action_mainFragment_to_recitersFragment)
            }

            }
        }

    fun setItems(newItems: List<MainItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}