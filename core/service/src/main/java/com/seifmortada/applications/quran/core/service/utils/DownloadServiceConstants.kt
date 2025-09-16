package com.seifmortada.applications.quran.core.service.utils

object DownloadServiceConstants {

    // Service Actions
    const val ACTION_START_DOWNLOAD = "com.seifmortada.quran.action.START_DOWNLOAD"
    const val ACTION_CANCEL_DOWNLOAD = "com.seifmortada.quran.action.CANCEL_DOWNLOAD"
    const val ACTION_CANCEL_DOWNLOAD_BY_ID = "com.seifmortada.quran.action.CANCEL_DOWNLOAD_BY_ID"

    // Intent Extras
    const val EXTRA_DOWNLOAD_REQUEST = "extra_download_request"
    const val EXTRA_DOWNLOAD_ID = "extra_download_id"

    // Broadcast Actions
    const val BROADCAST_DOWNLOAD_STATUS_CHANGED = "com.seifmortada.quran.DOWNLOAD_STATUS_CHANGED"

    // Notification Configuration
    const val NOTIFICATION_ID_DOWNLOAD = 3001
    const val NOTIFICATION_CHANNEL_ID = "quran_app_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Quran App"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications for Quran app services"

    // Download Configuration
    const val DOWNLOAD_BUFFER_SIZE = 8 * 1024 // 8KB buffer
    const val PROGRESS_UPDATE_INTERVAL = 1000L // Update progress every 1 second
    const val PROGRESS_NOTIFICATION_INTERVAL = 5 // Update notification every 5%
    const val CONNECTION_TIMEOUT = 30_000 // 30 seconds
    const val READ_TIMEOUT = 30_000 // 30 seconds

    // Error Messages
    const val ERROR_MSG_PERMISSION_DENIED = "Storage permission is required for downloads"
    const val ERROR_MSG_NETWORK_UNAVAILABLE = "Network connection is not available"
    const val ERROR_MSG_INVALID_URL = "Invalid download URL provided"
    const val ERROR_MSG_FILE_EXISTS = "File already exists"
    const val ERROR_MSG_STORAGE_FULL = "Insufficient storage space"
    const val ERROR_MSG_UNKNOWN = "An unknown error occurred"
}