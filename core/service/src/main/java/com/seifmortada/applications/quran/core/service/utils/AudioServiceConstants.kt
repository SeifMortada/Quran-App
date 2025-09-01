package com.seifmortada.applications.quran.core.service.utils

import android.R

const val AUDIO_CHANNEL_ID = "quran_app_channel"
const val DOWNLOAD_CHANNEL_ID = "quran_app_channel" // Also use same channel for consistency

// Audio player actions
const val REWIND = "rewind"
const val FORWARD = "forward"
const val PLAYPAUSE = "play-pause"
const val SEEK_TO = "seek-to"
const val AUDIO_LOAD = "audio-load"

object AudioServiceConstants {
    var notificationIcon: Int = R.drawable.ic_media_play
    var appLogo: Int = R.drawable.ic_media_play
    var rewindIcon: Int = R.drawable.ic_media_rew
    var playIcon: Int = R.drawable.ic_media_play
    var pauseIcon: Int = R.drawable.ic_media_pause
    var forwardIcon: Int = R.drawable.ic_media_ff
}
