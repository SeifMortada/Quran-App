package com.seifmortada.applications.quran.data.api_result

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val errorMessage: String) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}