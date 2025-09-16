package com.seifmortada.applications.quran.core.data.repository.reciters.surah_recitation

import android.content.Context
import com.seifmortada.applications.quran.core.data.datasource.RemoteDataSource
import com.seifmortada.applications.quran.core.domain.repository.reciters.surah_recitation.SurahRecitationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import com.seifmortada.applications.quran.core.domain.model.download.*

class SurahRecitationRepositoryImpl(
    private val context: Context,
    private val remote: RemoteDataSource
) : SurahRecitationRepository {

    override suspend fun getSurahRecitation(
        server: String,
        surahNumber: String
    ): Flow<DownloadProgress> = flow {

        val outputFile = provideOutputFile(surahNumber, server)

        if (outputFile.exists() && outputFile.length() > 0) {
            emit(
                DownloadProgress(
                    downloadedBytes = outputFile.length(),
                    totalBytes = outputFile.length(),
                    progress = 1f
                )
            )
            return@flow
        }

        val result = remote.retrieveSurahRecitation(surahNumber, server)
        if (result.isFailure) throw Exception(result.exceptionOrNull())
        else {
            emit(
                DownloadProgress(
                    downloadedBytes = 0L,
                    totalBytes = 0L,
                    progress = 0f
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    private fun provideOutputFile(surahNumber: String, telawah: String): File {
        val safeName = "surah_${surahNumber}_" + telawah.hashCode() + ".mp3"
        return File(context.cacheDir, safeName)
    }
}
