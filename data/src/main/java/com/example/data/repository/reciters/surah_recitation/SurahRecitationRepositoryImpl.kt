package com.example.data.repository.reciters.surah_recitation

import android.content.Context
import com.example.data.datasource.RemoteDataSource
import com.example.data.rest.apis.RecitersApi
import com.example.domain.model.NetworkResult
import com.example.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import com.example.domain.usecase.DownloadProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import android.os.Build

class SurahRecitationRepositoryImpl(
    private val context: Context,
    private val remote: RemoteDataSource
) : SurahRecitationRepository {

    override suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress> = flow {
        // Note: This method signature needs to be updated to include reciter info
        // For now, using a fallback approach
        val outputFile = provideOutputFile(surahNumber, server)

        if (outputFile.exists() && outputFile.length() > 0) {
            emit(
                DownloadProgress(
                    downloadedBytes = outputFile.length(),
                    totalBytes = outputFile.length(),
                    progress = 1f,
                    localPath = outputFile.absolutePath
                )
            )
            return@flow
        }

        // else download from network
        val result = remote.retrieveSurahRecitation(surahNumber, server)
        if (result.isFailure) throw Exception(result.exceptionOrNull())
        else {
            val downloadUrl = result.getOrNull()!!
            // Return the download URL so the ViewModel can use it
            // We put the URL in the localPath field for the ViewModel to access
            emit(
                DownloadProgress(
                    downloadedBytes = 0L,
                    totalBytes = 0L,
                    progress = 0f,
                    localPath = downloadUrl
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    private fun provideOutputFile(surahNumber: String, telawah: String): File {
        val safeName = "surah_${surahNumber}_" + telawah.hashCode() + ".mp3"
        return File(context.cacheDir, safeName)
    }
}
