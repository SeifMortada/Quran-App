package com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NetworkResult
import com.example.domain.model.SurahModel
import com.example.domain.usecase.GetSurahByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SurahRecitationViewModel(
 //   private val surahRecitationRepository: com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository,
    private val getSurahByIdUseCase: GetSurahByIdUseCase
) :
    ViewModel() {

    val errorState: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    val surahRecitationResponse: MutableLiveData<NetworkResult<String>> = MutableLiveData()
    val surahByIdResponse: MutableLiveData<SurahModel?> = MutableLiveData()

    fun fetchRecitation(server: String, surahNumber: Int) =
        viewModelScope.launch {
//            surahRecitationResponse.value = async(Dispatchers.IO) {
//                com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository.getSurahRecitation(
//                    server,
//                    surahNumber.toString()
//                )
//            }.await()
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