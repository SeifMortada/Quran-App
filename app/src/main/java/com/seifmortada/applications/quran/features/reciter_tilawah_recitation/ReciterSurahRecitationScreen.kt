package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import android.media.MediaPlayer
import android.widget.ToggleButton
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SurahModel
import com.example.domain.model.VerseModel
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.seifmortada.applications.quran.core.ui.composables.ForceRightOrLeft
import com.seifmortada.applications.quran.core.ui.composables.LanguagePreviews
import com.seifmortada.applications.quran.core.ui.composables.ThemePreviews

@Composable
fun ReciterSurahRecitationRoute(
    surahId: Int,
    server: String,
    onBackClicked: () -> Unit,
    viewModel: SurahRecitationViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(surahId, server) {
        viewModel.fetchRecitation(server, surahId)
    }
    ReciterSurahRecitationScreen(
        state = state,
        onBackClicked = onBackClicked
    )
}

@Composable
fun ReciterSurahRecitationScreen(
    modifier: Modifier = Modifier,
    state: SurahRecitationState,
    onBackClicked: () -> Unit = {}
) {
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    Scaffold(
        topBar = {
            SearchTopAppBar(
                title = state.currentSurah?.name ?: "Surah Recitation",
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

            when (val dState = state.downloadState) {
                is DownloadState.Idle -> {
                    // Nothing yet
                }

                is DownloadState.InProgress -> {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Downloading ${(dState.progress * 100).toInt()}%")
                        LinearProgressIndicator(
                            progress = { dState.progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is DownloadState.Finished -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        SurahDisplay(surah = state.currentSurah!!)
                    }
                    AudioPlayer(
                        title = state.title,
                        audioUrl = dState.filePath,
                        mediaPlayer = mediaPlayer
                    )
                }

                is DownloadState.Error -> {
                    ShowErrorMessage(errorMessage = dState.message)
                }
            }

            /*
                            state.isLoading -> CircularProgressIndicator()
                            state.isError.isNotEmpty() -> ShowErrorMessage(errorMessage = state.isError)
                            state.audioUrl.isNotEmpty() -> {
                              Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize()
                                ) {
                                    Text(text = "Audio Url: ${state.title}")
                                    LinearProgressIndicator(
                                    progress = { state.progress },
                                    modifier = Modifier.fillMaxWidth(),
                                    color = ProgressIndicatorDefaults.linearColor,
                                    trackColor = ProgressIndicatorDefaults.linearTrackColor,
                                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                                    )
                             //       SurahDisplay(surah = state.currentSurah!!)
                              //  }
            */
            /*                    AudioPlayer(
                                    title = state.title,
                                    audioUrl = state.audioUrl,
                                    mediaPlayer = mediaPlayer
                                )*//*

                }
            }
                state.downloadState
*/

        }
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
            // Surah Header
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

            // Verses list
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
            // Verse number inside a circle
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
fun ShowErrorMessage(errorMessage: String) {
    Text(errorMessage, color = MaterialTheme.colorScheme.primary)
}

@Composable
fun AudioPlayer(
    title: String,
    audioUrl: String,
    mediaPlayer: MediaPlayer
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableIntStateOf(0) }
    var duration by remember { mutableIntStateOf(0) }
    var isPrepared by remember { mutableStateOf(false) }
    LaunchedEffect(audioUrl) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                duration = it.duration
                isPrepared = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    LaunchedEffect(isPlaying) {
        while (isPlaying && isPrepared) {
            currentPosition = mediaPlayer.currentPosition
            delay(1000)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        ProgressBarSlider(
            title = title,
            currentPosition = currentPosition,
            duration = duration,
            onValueChange = {
                if (isPrepared) {
                    mediaPlayer.seekTo(it.toInt())
                    currentPosition = it.toInt()
                }
            }
        )

        PlayPauseRow(
            isPlaying = isPlaying,
            onReplayClicked = {
                if (isPrepared) {
                    val newPosition = (mediaPlayer.currentPosition - 10000).coerceAtLeast(0)
                    mediaPlayer.seekTo(newPosition)
                    currentPosition = newPosition
                }
            },
            onPlayClicked = {
                if (isPrepared) {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        isPlaying = false
                    } else {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                }
            },
            onFastForwardClicked = {
                if (isPrepared) {
                    val newPosition = (mediaPlayer.currentPosition + 10000).coerceAtMost(duration)
                    mediaPlayer.seekTo(newPosition)
                    currentPosition = newPosition
                }
            }
        )
    }
}


@Composable
fun ProgressBarSlider(
    title: String,
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
        // Forward - secondary action
        OutlinedButton(
            onClick = onReplayClicked,
            modifier = Modifier
                .weight(1f)
                .semantics { role = Role.Button },
            shape = ButtonGroupDefaults.connectedLeadingButtonShape,
        ) {
            Icon(Icons.Default.Replay10, contentDescription = "Replay 10 seconds")
        }

        // Play / Pause - primary action (expressive)
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
        // Rewind - secondary action
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

/*
@LanguagePreviews
@ThemePreviews
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun ReciterSurahRecitationScreenPreview() {
    QuranAppTheme {
        ReciterSurahRecitationScreen(
            state = SurahRecitationState(
                audioUrl = "https://cdn.islamic.network/quran/audio-surah/128/ar.alafasy/1",
                fileSize = 123456789,
                title = "Surah Name",
                currentSurah = SurahModel(
                    id = 1,
                    name = "Name",
                    totalVerses = 100,
                    transliteration = "Transliteration",
                    type = "meccan",
                    verses = listOf(
                            VerseModel(id = 1, text = "Verse Text", surahId = 1),
                            VerseModel(id = 1, text = "Verse Text", surahId = 1),
                            VerseModel(id = 1, text = "Verse Text", surahId = 1),
                            VerseModel(id = 1, text = "Verse Text", surahId = 1)
                        )
                ),
                isError = "",
                isLoading = false
            )
        )
    }
}
*/

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
    /*
            var isPlaying by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                // Rewind - secondary action
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .semantics { role = Role.Button },
                    shape = ButtonGroupDefaults.connectedTrailingButtonShape,
                ) {
                    Icon(Icons.Default.Replay10, contentDescription = "Rewind")
                }

                // Play / Pause - primary action (expressive)
                ToggleButton(
                    checked = isPlaying,
                    onCheckedChange = { isPlaying = it },
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


                // Forward - secondary action
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .semantics { role = Role.Button },
                    shape = ButtonGroupDefaults.connectedLeadingButtonShape,
                ) {
                    Icon(Icons.Default.Forward10, contentDescription = "Fast Forward")
                }
            }
        }*/

}