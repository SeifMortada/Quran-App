package com.seifmortada.applications.quran.ui.fragment.reciters.surah_recitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.data.repository.reciters.surah_recitation.SurahRecitationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SurahRecitationViewModel(private val surahRecitationRepository: SurahRecitationRepository) :
    ViewModel() {

    val errorState: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    val surahRecitationResponse: MutableLiveData<NetworkResult<String>> = MutableLiveData()

    fun fetchRecitation(server: String, surahNumber: String) =
        viewModelScope.launch {
            surahRecitationResponse.value = async(Dispatchers.IO) {
                surahRecitationRepository.getSurahRecitation(
                    server,
                    surahNumber
                )
            }.await()
        }

    fun resetErrorState() {
        viewModelScope.launch(Dispatchers.Main) {
            errorState.value = Pair(false, "")
        }
    }

    fun resetLoadingState() {
        viewModelScope.launch(Dispatchers.Main) {
            loadingState.value = false
        }
    }
}