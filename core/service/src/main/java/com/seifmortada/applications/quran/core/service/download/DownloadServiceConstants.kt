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
    const val CONNECTION_TIMEOUT = 30_000
    const val BUFFER_SIZE = 8 * 1024
    const val PROGRESS_UPDATE_INTERVAL = 1000L
}