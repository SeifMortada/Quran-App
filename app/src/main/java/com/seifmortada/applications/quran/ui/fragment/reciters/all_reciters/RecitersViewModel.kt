package com.seifmortada.applications.quran.ui.fragment.reciters.all_reciters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.quran.domain.repository.reciters.all_reciters.RecitersRepository
import com.seifmortada.applications.quran.data.rest.utils.NetworkResult
import com.seifmortada.applications.quran.data.rest.response.reciters.RecitersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecitersViewModel(private val recitersRepository: RecitersRepository) : ViewModel() {

    val recitersResponse: MutableLiveData<NetworkResult<RecitersResponse>> = MutableLiveData()

    init {
        fetchAllReciters()
    }

    private fun fetchAllReciters() {
        viewModelScope.launch(Dispatchers.IO) {
            recitersResponse.postValue(NetworkResult.Loading)
            recitersResponse.postValue(recitersRepository.getAllReciters())
        }
    }
}