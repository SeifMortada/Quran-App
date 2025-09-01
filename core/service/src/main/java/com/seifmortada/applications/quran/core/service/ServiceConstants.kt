package com.seifmortada.applications.quran.core.service

// FIXED: Use the same channel ID as Application class
const val AUDIO_CHANNEL_ID = "quran_app_channel"
const val DOWNLOAD_CHANNEL_ID = "quran_app_channel" // Also use same channel for consistency

// Audio player actions
const val REWIND = "rewind"
const val FORWARD = "forward"
const val PLAYPAUSE = "play-pause"
const val SEEK_TO = "seek-to"
const val AUDIO_LOAD = "audio-load"

// Resource identifiers - These should be passed from the app module
object ServiceResources {
    var notificationIcon: Int = android.R.drawable.ic_media_play
    var appLogo: Int = android.R.drawable.ic_media_play
    var rewindIcon: Int = android.R.drawable.ic_media_rew
    var playIcon: Int = android.R.drawable.ic_media_play
    var pauseIcon: Int = android.R.drawable.ic_media_pause
    var forwardIcon: Int = android.R.drawable.ic_media_ff
}
