package com.seifmortada.applications.quran.ui.fragment.quran.surah

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.seifmortada.applications.quran.data.remote.utils.NetworkResult
import com.seifmortada.applications.quran.data.repository.surah.SurahRepository
import com.seifmortada.applications.quran.utils.AndroidUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SurahViewModel(private val surahRepository: SurahRepository) : ViewModel() {

    val errorState: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    val ayahRecitation: MutableLiveData<NetworkResult<String>> = MutableLiveData()

    val pauseState: MutableLiveData<Boolean> = MutableLiveData()

    val loadingState: MutableLiveData<Boolean> = MutableLiveData()

    val ayahEnded: MutableLiveData<Boolean> = MutableLiveData()

    lateinit var exoPlayer: ExoPlayer
    fun getAyahRecitation(surahNumber: String, ayahNumber: String) =
        viewModelScope.launch(Dispatchers.IO) {
            ayahRecitation.postValue(NetworkResult.Loading)
            val globalAyahNumber =
                async(Dispatchers.Default) {
                    AndroidUtils.calculateGlobalAyahNumber(
                        surahNumber.toInt(),
                        ayahNumber.toInt()
                    )
                }.await()
            try {
                ayahRecitation.postValue(surahRepository.getAyahRecitation(globalAyahNumber.toString()))

            } catch (e: Exception) {
                postError(e.message.toString())
            }
        }


    private suspend fun postError(message: String) {
        withContext(Dispatchers.Main) {
            errorState.value = Pair(true, message)
            resetLoadingState()
        }
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

    fun pauseAyahRecitation() {
        pauseState.value = true
    }

    fun resumeAyahRecitation() {
        pauseState.value = false
    }
}