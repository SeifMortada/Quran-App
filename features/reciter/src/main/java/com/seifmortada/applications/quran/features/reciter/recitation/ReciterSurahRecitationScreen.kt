package com.seifmortada.applications.quran.features.reciter.recitation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import android.os.IBinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.domain.model.VerseModel
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.core.ui.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seifmortada.applications.quran.core.service.AudioPlayerService
import com.seifmortada.applications.quran.core.ui.composables.ForceRightOrLeft
import com.seifmortada.applications.quran.core.ui.composables.LanguagePreviews
import com.seifmortada.applications.quran.core.ui.composables.ShowErrorMessage
import com.seifmortada.applications.quran.core.ui.composables.ThemePreviews
import com.seifmortada.applications.quran.utils.SearchTopAppBar

@Composable
fun ReciterSurahRecitationRoute(
    surahId: Int,
    server: String,
    onBackClicked: () -> Unit,
    reciterName: String = "Unknown Reciter",
    viewModel: ReciterSurahRecitationViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.event.collectAsState(initial = FileDownloadEvent.Idle)
    val context = LocalContext.current
    val service = rememberAudioService()

    LaunchedEffect(surahId, server, reciterName) {
        viewModel.fetchRecitation(
            context = context,
            server = server,
            surahNumber = surahId,
            reciterName = reciterName,
            surahNameEn = null,
            surahNameAr = null
        )
    }

    LaunchedEffect(service) {
        service?.let { viewModel.bindService(it) }
    }

    ReciterSurahRecitationScreen(
        state = state,
        events = events,
        audioActions = { action -> viewModel.sendEvent(context, action) },
        onBackClicked = onBackClicked,
        onRetryDownload = { viewModel.retryDownload() },
        onCancelDownload = { viewModel.cancelDownload() }
    )
}

@Composable
fun ReciterSurahRecitationScreen(
    state: ReciterSurahRecitationUiState,
    events: FileDownloadEvent,
    audioActions: (AudioPlayerAction) -> Unit,
    onBackClicked: () -> Unit,
    onRetryDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDownloadDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchTopAppBar(
                title = state.currentSurah?.name ?: stringResource(R.string.surah_recitation),
                onBackClick = onBackClicked,
                onSearchClick = {}
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (events) {
                is FileDownloadEvent.Idle -> {
                    if (state.title.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.title,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                is FileDownloadEvent.Starting -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Initializing download...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is FileDownloadEvent.InProgress -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Progress percentage and size info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = events.getFormattedProgress(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (events.totalBytes > 0) {
                                Text(
                                    text = events.getFormattedSize(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress bar
                        LinearProgressIndicator(
                            progress = { events.progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Speed and time remaining info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (events.downloadSpeed > 0) {
                                Text(
                                    text = "Speed: ${events.getFormattedSpeed()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            if (events.estimatedTimeRemaining > 0) {
                                Text(
                                    text = "Time left: ${events.getFormattedTimeRemaining()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Cancel button
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = onCancelDownload,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text("Cancel Download")
                        }
                    }
                }

                is FileDownloadEvent.Finished -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        state.currentSurah?.let { surah ->
                            SurahDisplay(surah = surah)
                        }
                    }
                    AudioPlayer(
                        audioPlayerState = state.audioPlayerState,
                        audioActions = audioActions,
                        audioUrl = events.filePath
                    )
                }

                is FileDownloadEvent.Cancelled -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        state.currentSurah?.let { surah ->
                            SurahDisplay(surah = surah)
                        }
                    }

                    AudioPlayerPlaceholder(
                        onPlayAttempt = { showDownloadDialog = true }
                    )
                }

                is FileDownloadEvent.Error -> {
                    ShowErrorMessage(
                        errorMessage = events.message,
                        onRetry = {
                            onRetryDownload()
                        }
                    )
                }
            }
        }
    }

    if (showDownloadDialog) {
        DownloadConfirmationDialog(
            surahName = state.currentSurah?.name ?: stringResource(R.string.this_surah),
            onConfirm = {
                showDownloadDialog = false
                onRetryDownload()
            },
            onDismiss = {
                showDownloadDialog = false
            }
        )
    }
}

@Composable
private fun SurahDisplay(surah: SurahModel) {
    ForceRightOrLeft(forceRight = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = surah.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${surah.type} • ${surah.totalVerses} آيات",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (surah.transliteration.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = surah.transliteration,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(surah.verses) { verse ->
                    AyahItem(verse)
                }
            }
        }
    }
}

@Composable
private fun AyahItem(verse: VerseModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = verse.id.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = verse.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable
fun AudioPlayer(
    audioPlayerState: AudioPlayerState,
    audioActions: (AudioPlayerAction) -> Unit,
    audioUrl: String
) {

    val currentDuration = audioPlayerState.currentPosition
    val maxDuration = audioPlayerState.audio?.duration
    val isPlaying = audioPlayerState.isPlaying

    LaunchedEffect(audioUrl) {
        if (audioUrl != audioPlayerState.audio?.path) {
            audioActions(AudioPlayerAction.LoadAudioPlayer(audioUrl))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        ProgressBarSlider(
            currentPosition = currentDuration,
            duration = maxDuration ?: 0,
            onValueChange = { newPosition ->
                if (audioPlayerState.isPrepared) {
                    audioActions(AudioPlayerAction.SeekTo(newPosition.toInt()))
                }
            }
        )

        PlayPauseRow(
            isPlaying = isPlaying,
            onReplayClicked = {
                if (audioPlayerState.isPrepared) {
                    audioActions(AudioPlayerAction.FastRewind)
                }
            },
            onPlayClicked = { audioActions(AudioPlayerAction.PlayPause) },
            onFastForwardClicked = { audioActions(AudioPlayerAction.FastForward) }
        )
    }
}


@Composable
fun ProgressBarSlider(
    currentPosition: Int,
    duration: Int,
    onValueChange: (Float) -> Unit = {}
) {
    Slider(
        value = currentPosition.toFloat(),
        onValueChange = {
            onValueChange(it)
        },
        valueRange = 0f..duration.toFloat(),
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(formatTime(currentPosition), style = MaterialTheme.typography.bodySmall)
        Text(formatTime(duration), style = MaterialTheme.typography.bodySmall)
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlayPauseRow(
    onReplayClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onFastForwardClicked: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        OutlinedButton(
            onClick = onReplayClicked,
            modifier = Modifier
                .weight(1f)
                .semantics { role = Role.Button },
            shape = ButtonGroupDefaults.connectedLeadingButtonShape,
        ) {
            Icon(Icons.Default.Replay10, contentDescription = "Replay 10 seconds")
        }

        ToggleButton(
            checked = isPlaying,
            onCheckedChange = { onPlayClicked() },
            modifier = Modifier
                .weight(1.5f)
                .semantics { role = Role.Button },
            shapes = ButtonGroupDefaults.connectedMiddleButtonShapes(),
            colors = ToggleButtonDefaults.toggleButtonColors(
                checkedContainerColor = MaterialTheme.colorScheme.primary,
                checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        ) {
            Icon(
                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
            )
        }
        OutlinedButton(
            onClick = onFastForwardClicked,
            modifier = Modifier
                .weight(1f)
                .semantics { role = Role.Button },
            shape = ButtonGroupDefaults.connectedTrailingButtonShape,
        ) {
            Icon(Icons.Default.Forward10, contentDescription = "Rewind")
        }
    }
}

fun formatTime(millis: Int): String {
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var i = 0
    var sizeTemp = size
    while (sizeTemp > 1024 && i < units.size - 1) {
        sizeTemp /= 1024
        i++
    }
    return String.format("%.2f %s", sizeTemp.toDouble(), units[i])
}

@Composable
fun rememberAudioService(): AudioPlayerService? {
    val context = LocalContext.current
    var service by remember { mutableStateOf<AudioPlayerService?>(null) }
    val connection = remember { mutableStateOf<ServiceConnection?>(null) }

    DisposableEffect(Unit) {
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                service = (binder as? AudioPlayerService.AudioPlayerBinder)?.getService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                service = null
            }
        }

        val intent = Intent(context, AudioPlayerService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        connection.value = serviceConnection

        onDispose {
            connection.value?.let { context.unbindService(it) }
            service = null
        }
    }

    return service
}

@Composable
fun AudioPlayerPlaceholder(
    onPlayAttempt: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Disabled progress bar
        Slider(
            value = 0f,
            onValueChange = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("00:00", style = MaterialTheme.typography.bodySmall)
            Text("--:--", style = MaterialTheme.typography.bodySmall)
        }

        // Placeholder play button that triggers download dialog
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = onPlayAttempt,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Download to play")
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.download_to_play))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadConfirmationDialog(
    surahName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.download_required),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(R.string.download_required_message, surahName),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            androidx.compose.material3.Button(onClick = onConfirm) {
                Text(stringResource(R.string.download))
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@LanguagePreviews
@ThemePreviews
@Composable
private fun PreviewReciterSurahRecitationScreen() {
    QuranAppTheme {
        LinearProgressIndicator(
            progress = { 0.5f },
            modifier = Modifier
                .fillMaxWidth()
                .size(50.dp),
            color = ProgressIndicatorDefaults.linearColor,

            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}
