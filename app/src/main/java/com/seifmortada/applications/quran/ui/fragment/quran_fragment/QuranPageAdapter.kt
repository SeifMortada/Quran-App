//package com.seifmortada.applications.quran.ui.fragment.quran_fragment
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.seifmortada.applications.quran.R
//import com.seifmortada.applications.quran.databinding.ItemQuranPageBinding
//import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
//
//class QuranPageAdapter() : RecyclerView.Adapter<QuranPageAdapter.PageViewHolder>() {
//
//    private var pages: MutableList<QuranPage> = mutableListOf()
//
//    class PageViewHolder(val binding: ItemQuranPageBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(page: QuranPage) {
//            binding.tvPageNumber.text = "صفحة ${page.pageNumber}"
//            binding.tvHizbInfo.text = page.hizbInfo
//
//            // Clear previous ayahs
//            //binding.ayatContainer.removeAllViews()
//
//            // Add ayahs dynamically
//            page.ayahs.forEach { ayah ->
//                val ayahView = LayoutInflater.from(itemView.context)
//                    .inflate(R.layout.item_ayah, binding.ayatContainer, false)
//                val tvAyatText: TextView = ayahView.findViewById(R.id.tvAyatText)
//
//                tvAyatText.text = ayah.text +"{${ayah.number}}"
//                binding.ayatContainer.addView(ayahView)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
//        return PageViewHolder(
//            ItemQuranPageBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int = pages.size
//
//    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
//        val currentPage = pages[position]
//        holder.bind(currentPage)
//    }
//
//    fun setData(pages: MutableList<QuranPage>) {
//        this.pages = pages
//        notifyDataSetChanged()
//    }
//}
