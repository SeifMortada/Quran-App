package com.seifmortada.applications.quran.core.service.download

import android.content.Context
import android.util.Log
import com.seifmortada.applications.quran.core.domain.repository.*
import com.seifmortada.applications.quran.core.service.utils.DownloadServiceConstants
import com.seifmortada.applications.quran.core.ui.QuranFileManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

/**
 * Core download manager that handles download operations
 * Follows clean architecture principles with proper separation of concerns
 */
class DownloadManager(
    private val context: Context,
    private val fileManager: QuranFileManager
) {
    companion object {
        private const val TAG = "DownloadManager"
    }

    private val activeDownloads = ConcurrentHashMap<String, DownloadJob>()
    private val downloadStatusFlow = MutableStateFlow<DownloadStatus>(DownloadStatus.Idle)
    private val activeDownloadsFlow = MutableStateFlow<List<DownloadInfo>>(emptyList())

    private data class DownloadJob(
        val downloadInfo: DownloadInfo,
        val job: Job
    )

    /**
     * Starts a new download
     */
    fun startDownload(downloadRequest: DownloadRequest): Flow<DownloadStatus> = callbackFlow {
        val downloadId = downloadRequest.downloadId

        // Check if already downloading
        if (activeDownloads.containsKey(downloadId)) {
            trySend(
                DownloadStatus.Failed(
                    "Download already in progress",
                    DownloadErrorCode.UNKNOWN
                )
            )
            close()
            return@callbackFlow
        }

        // Check if file already exists
        if (isFileAlreadyDownloaded(downloadRequest)) {
            val filePath = getExistingFilePath(downloadRequest)
            if (filePath != null) {
                trySend(DownloadStatus.Completed(filePath))
                close()
                return@callbackFlow
            }
        }

        trySend(DownloadStatus.Starting)
        downloadStatusFlow.value = DownloadStatus.Starting

        val downloadJob = CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            try {
                performDownload(downloadRequest) { status ->
                    trySend(status)
                    downloadStatusFlow.value = status
                }
            } catch (e: CancellationException) {
                trySend(DownloadStatus.Cancelled)
                downloadStatusFlow.value = DownloadStatus.Cancelled
            } catch (e: Exception) {
                Log.e(TAG, "Download failed", e)
                val errorStatus = DownloadStatus.Failed(
                    error = e.message ?: DownloadServiceConstants.ERROR_MSG_UNKNOWN,
                    errorCode = mapExceptionToErrorCode(e)
                )
                trySend(errorStatus)
                downloadStatusFlow.value = errorStatus
            } finally {
                activeDownloads.remove(downloadId)
                updateActiveDownloadsList()
            }
        }

        val downloadInfo = DownloadInfo(
            downloadId = downloadId,
            downloadRequest = downloadRequest,
            status = DownloadStatus.Starting
        )

        activeDownloads[downloadId] = DownloadJob(downloadInfo, downloadJob)
        updateActiveDownloadsList()

        awaitClose {
            downloadJob.cancel()
            activeDownloads.remove(downloadId)
            updateActiveDownloadsList()
        }
    }

    /**
     * Cancels all downloads
     */
    fun cancelAllDownloads() {
        activeDownloads.values.forEach { it.job.cancel() }
        activeDownloads.clear()
        updateActiveDownloadsList()
        downloadStatusFlow.value = DownloadStatus.Cancelled
    }

    /**
     * Cancels a specific download
     */
    fun cancelDownload(downloadId: String) {
        activeDownloads[downloadId]?.job?.cancel()
        activeDownloads.remove(downloadId)
        updateActiveDownloadsList()
    }

    /**
     * Gets the current download status
     */
    fun getCurrentDownloadStatus(): Flow<DownloadStatus> = downloadStatusFlow.asStateFlow()

    /**
     * Gets all active downloads
     */
    fun getActiveDownloads(): Flow<List<DownloadInfo>> = activeDownloadsFlow.asStateFlow()

    /**
     * Checks if a file is already downloaded
     */
    fun isFileAlreadyDownloaded(downloadRequest: DownloadRequest): Boolean {
        return fileManager.surahFileExists(
            downloadRequest.reciterName,
            downloadRequest.serverUrl,
            downloadRequest.surahNumber,
            downloadRequest.surahNameAr,
            downloadRequest.surahNameEn
        )
    }

    /**
     * Gets the file path for an already downloaded file
     */
    fun getExistingFilePath(downloadRequest: DownloadRequest): String? {
        val file = fileManager.getSurahFilePath(
            downloadRequest.reciterName,
            downloadRequest.serverUrl,
            downloadRequest.surahNumber,
            downloadRequest.surahNameAr,
            downloadRequest.surahNameEn
        )
        return if (file.exists() && file.length() > 0) file.absolutePath else null
    }

    private suspend fun performDownload(
        downloadRequest: DownloadRequest,
        onStatusUpdate: (DownloadStatus) -> Unit
    ) {
        val outputFile = fileManager.getSurahFilePath(
            downloadRequest.reciterName,
            downloadRequest.serverUrl,
            downloadRequest.surahNumber,
            downloadRequest.surahNameAr,
            downloadRequest.surahNameEn
        )

        // Ensure parent directory exists
        outputFile.parentFile?.mkdirs()

        val connection = withContext(Dispatchers.IO) {
            URL(downloadRequest.downloadUrl).openConnection() as HttpURLConnection
        }

        connection.apply {
            requestMethod = "GET"
            connectTimeout = DownloadServiceConstants.CONNECTION_TIMEOUT
            readTimeout = DownloadServiceConstants.READ_TIMEOUT
            connect()
        }

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            connection.disconnect()
            throw Exception("Server returned HTTP $responseCode")
        }

        val totalBytes = connection.contentLengthLong
        if (totalBytes <= 0) {
            connection.disconnect()
            throw Exception("Could not determine file size")
        }

        withContext(Dispatchers.IO) {
            connection.inputStream.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    val buffer = ByteArray(DownloadServiceConstants.DOWNLOAD_BUFFER_SIZE)
                    var bytesRead: Int
                    var downloadedBytes = 0L
                    var lastUpdateTime = System.currentTimeMillis()
                    var lastProgressPercent = 0

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        ensureActive() // Check for cancellation

                        outputStream.write(buffer, 0, bytesRead)
                        downloadedBytes += bytesRead

                        val currentTime = System.currentTimeMillis()
                        val progress = downloadedBytes.toFloat() / totalBytes
                        val progressPercent = (progress * 100).toInt()

                        // Update progress based on time interval or percentage change
                        if (currentTime - lastUpdateTime >= DownloadServiceConstants.PROGRESS_UPDATE_INTERVAL ||
                            progressPercent != lastProgressPercent
                        ) {

                            val downloadSpeed = if (currentTime > lastUpdateTime) {
                                (bytesRead * 1000L) / (currentTime - lastUpdateTime)
                            } else 0L

                            val estimatedTimeRemaining = if (downloadSpeed > 0) {
                                (totalBytes - downloadedBytes) / downloadSpeed * 1000
                            } else 0L

                            val downloadProgress = DownloadProgress(
                                progress = progress,
                                downloadedBytes = downloadedBytes,
                                totalBytes = totalBytes,
                                downloadSpeed = downloadSpeed,
                                estimatedTimeRemaining = estimatedTimeRemaining
                            )

                            onStatusUpdate(DownloadStatus.InProgress(downloadProgress))
                            lastUpdateTime = currentTime
                            lastProgressPercent = progressPercent
                        }
                    }

                    outputStream.flush()
                }
            }
        }

        connection.disconnect()

        // Verify file was downloaded correctly
        if (!outputFile.exists() || outputFile.length() != totalBytes) {
            outputFile.delete()
            throw Exception("Download verification failed")
        }

        onStatusUpdate(DownloadStatus.Completed(outputFile.absolutePath))
    }

    private fun updateActiveDownloadsList() {
        val downloadList = activeDownloads.values.map { it.downloadInfo }
        activeDownloadsFlow.value = downloadList
    }

    private fun mapExceptionToErrorCode(exception: Exception): DownloadErrorCode {
        return when {
            exception.message?.contains("timeout", true) == true -> DownloadErrorCode.TIMEOUT
            exception.message?.contains("network", true) == true -> DownloadErrorCode.NETWORK_ERROR
            exception.message?.contains("storage", true) == true -> DownloadErrorCode.STORAGE_ERROR
            exception.message?.contains(
                "permission",
                true
            ) == true -> DownloadErrorCode.PERMISSION_ERROR

            exception.message?.contains("url", true) == true -> DownloadErrorCode.INVALID_URL
            exception.message?.contains("server", true) == true -> DownloadErrorCode.SERVER_ERROR
            else -> DownloadErrorCode.UNKNOWN
        }
    }
}