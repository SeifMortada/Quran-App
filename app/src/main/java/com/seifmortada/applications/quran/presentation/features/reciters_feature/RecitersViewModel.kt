package com.seifmortada.applications.quran.presentation.features.reciters_feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NetworkResult
import com.example.domain.model.ReciterModel
import com.example.domain.usecase.GetAllRecitersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RecitersViewModel(private val getAllRecitersUseCase: GetAllRecitersUseCase) : ViewModel() {

    val recitersResponse: MutableLiveData<NetworkResult<List<ReciterModel>>?> = MutableLiveData()

    init {
        fetchAllReciters()
    }

    private fun fetchAllReciters() {
        viewModelScope.launch(Dispatchers.IO) {
            recitersResponse.postValue(getAllRecitersUseCase())
            Timber.d("RecitersViewModel, fetchAllReciters: ${recitersResponse.value}")
        }
    }
}