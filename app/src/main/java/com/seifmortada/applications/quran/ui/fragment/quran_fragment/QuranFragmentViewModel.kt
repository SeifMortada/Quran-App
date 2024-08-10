package com.seifmortada.applications.quran.ui.fragment.quran_fragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seifmortada.applications.quran.data.api_result.NetworkResult
import com.seifmortada.applications.quran.data.repository.quran.QuranRepository
import com.seifmortada.applications.quran.data.local.entity.quran.QuranPage
import com.seifmortada.applications.quran.domain.model.response.quran.QuranResponse
import com.seifmortada.applications.quran.domain.model.response.quran.Surah
import com.tazkiyatech.quran.sdk.database.QuranDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class QuranFragmentViewModel(private val quranRepo: QuranRepository, private val context: Context) :
    ViewModel() {

    private val convertResult: MutableLiveData<MutableList<QuranPage>> = MutableLiveData()


    var convertResponse: LiveData<MutableList<QuranPage>>
        get() = convertResult
        set(value) {
            convertResult.value = value.value
        }

    var allSurahs: MutableLiveData<List<Surah>> = MutableLiveData()

     val quranResponse: MutableLiveData<QuranResponse> = MutableLiveData()

    init {
        loadQuranData()
    }

    private fun loadQuranData() {
        viewModelScope.launch {
            val quranDatabase = QuranDatabase(context)
            quranDatabase.openDatabase()
            // Convert to pages
            //quranResponse.value = quranDatabase.getSurahNames()
                //async(Dispatchers.Default) { returnJsonResponse() }.await()
        }
    }

    private fun returnJsonResponse(): QuranResponse {
        val gson = Gson()
        val jsonFile = context.assets.open("quran.json")
        val reader = InputStreamReader(jsonFile)
        val quranType = object : TypeToken<QuranResponse>() {}.type
        return gson.fromJson(reader, quranType)
    }

    fun convertResponseToPages(quranResponse: QuranResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            convertResult.postValue(QuranResponse.convertToQuranPage(quranResponse))
        }

}