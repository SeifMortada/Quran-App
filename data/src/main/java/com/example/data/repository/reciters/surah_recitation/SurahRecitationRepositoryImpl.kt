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

class SurahRecitationRepositoryImpl(
    private val context: Context,
    private val remote: RemoteDataSource
) : SurahRecitationRepository {

    override suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress> = flow {
        val outputFile = provideOutputFile(surahNumber, server)

        if (outputFile.exists()) {
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
        else emitAll(downloadFile(result.getOrNull()!!, outputFile))
    }.flowOn(Dispatchers.IO)

    private fun provideOutputFile(surahNumber: String, telawah: String): File {
        val safeName = "surah_${surahNumber}_" + telawah.hashCode() + ".mp3"
        return File(context.cacheDir, safeName)
    }

    private fun downloadFile(url: String, outputFile: File): Flow<DownloadProgress> = flow {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val totalBytes = connection.contentLengthLong
        if (totalBytes <= 0) throw Exception("Could not get file size")

        val inputStream = connection.inputStream
        val outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        var downloadedBytes = 0L

        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
            downloadedBytes += bytesRead
            emit(
                DownloadProgress(
                    downloadedBytes = downloadedBytes,
                    totalBytes = totalBytes,
                    progress = downloadedBytes.toFloat() / totalBytes,
                    localPath = null
                )
            )
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
        connection.disconnect()

        // final emit with local path
        emit(
            DownloadProgress(
                downloadedBytes = totalBytes,
                totalBytes = totalBytes,
                progress = 1f,
                localPath = outputFile.absolutePath
            )
        )
    }
}
