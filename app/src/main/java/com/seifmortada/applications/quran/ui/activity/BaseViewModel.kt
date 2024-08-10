package com.seifmortada.applications.quran.ui.activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
import com.seifmortada.applications.quran.domain.model.response.quran.QuranResponse
import com.seifmortada.applications.quran.domain.model.response.quran.Surah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader


class BaseViewModel(context: Context) : ViewModel() {

    private val convertResult: MutableLiveData<MutableList<QuranPage>> = MutableLiveData()


    var convertResponse: LiveData<MutableList<QuranPage>>
        get() = convertResult
        set(value) {
            convertResult.value = value.value
        }

    var allSurahs: MutableLiveData<List<Surah>> = MutableLiveData()

    private val quranResponse: MutableLiveData<QuranResponse> = MutableLiveData()

    init {
       // quranResponse.value = loadQuranData(context)
    }

    private fun loadQuranData(context: Context): QuranResponse {
        val gson = Gson()
        val jsonFile = context.assets.open("quran.json")
        val reader = InputStreamReader(jsonFile)
        val quranType = object : TypeToken<QuranResponse>() {}.type
        // Convert to pages
        return gson.fromJson(reader, quranType)
    }

    fun convertResponseToPages(quranResponse: QuranResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            convertResult.postValue(QuranResponse.convertToQuranPage(quranResponse))
        }

    fun getAllSurahs() {
        allSurahs.value = QuranResponse.getAllSurahs(quranResponse.value!!)
    }
}