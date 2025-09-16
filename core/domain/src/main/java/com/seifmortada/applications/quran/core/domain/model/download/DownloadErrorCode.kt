package com.seifmortada.applications.quran.core.domain.model.download

import java.io.Serializable

enum class DownloadErrorCode : Serializable {
    NETWORK_ERROR,
    STORAGE_ERROR,
    PERMISSION_ERROR,
    FILE_ALREADY_EXISTS,
    INVALID_URL,
    SERVER_ERROR,
    TIMEOUT,
    UNKNOWN
}