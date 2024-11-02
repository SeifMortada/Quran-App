package com.seifmortada.applications.quran.ui.fragment.reciters.surah_telawah

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.seifmortada.applications.quran.data.rest.utils.NetworkResult
import com.seifmortada.applications.quran.data.local.room.entities.quran.Surah
import com.seifmortada.applications.quran.domain.usecase.GetSurahByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SurahRecitationViewModel(
    private val surahRecitationRepository: SurahRecitationRepository,
    private val getSurahByIdUseCase: GetSurahByIdUseCase
) :
    ViewModel() {

    val errorState: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    val surahRecitationResponse: MutableLiveData<NetworkResult<String>> = MutableLiveData()
    val surahByIdResponse: MutableLiveData<Surah?> = MutableLiveData()

    fun fetchRecitation(server: String, surahNumber: Int) =
        viewModelScope.launch {
            surahRecitationResponse.value = async(Dispatchers.IO) {
                surahRecitationRepository.getSurahRecitation(
                    server,
                    surahNumber.toString()
                )
            }.await()
            getSurahById(surahNumber)
        }

    private fun getSurahById(surahNumber: Int) = viewModelScope.launch(Dispatchers.IO) {
        surahByIdResponse.postValue(getSurahByIdUseCase(surahNumber))
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