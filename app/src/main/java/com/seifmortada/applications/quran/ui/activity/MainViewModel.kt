package com.seifmortada.applications.quran.ui.activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.model.quran.Quran
import com.seifmortada.applications.quran.data.model.quran.Surah

class MainViewModel(val context: Context):ViewModel() {

    // Lazy initialization of the Quran data
    val quranData: List<Surah> by lazy {
        loadQuranData()
    }


    private  fun loadQuranData(): List<Surah> {
        val json = context.assets.open("quran.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Surah>>() {}.type
        return Gson().fromJson(json, type)    }
}