package com.seifmortada.applications.quran.features.reciter_tilawah_recitation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import android.media.MediaPlayer
import android.text.format.Formatter.formatFileSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SurahModel
import com.example.domain.model.VerseModel
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import kotlinx.coroutines.delay

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
                title = "Reciter Surah Recitation",
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

            when {
                state.isLoading -> CircularProgressIndicator()
                state.isError.isNotEmpty() -> ShowErrorMessage(errorMessage = state.isError)
                state.audioUrl.isNotEmpty() -> {
                    Text(
                        text = "File Size: ${formatFileSize(LocalContext.current, state.fileSize)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        SurahDisplay(surah = state.currentSurah!!)
                    }
                    AudioPlayer(
                        title = state.title,
                        audioUrl = state.audioUrl,
                        mediaPlayer = mediaPlayer
                    )
                }
            }

        }
    }
}

@Composable
fun SurahDisplay(surah: SurahModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = surah.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "(${surah.type})",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(surah.verses) { verse ->
                    AyahItem(verse)
                }
            }
        }
    }

}


@Composable
fun AyahItem(verse: VerseModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = verse.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "(${verse.id})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.align(Alignment.End)
            )
        }
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
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(16.dp)
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
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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

@Composable
fun PlayPauseRow(
    onReplayClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onFastForwardClicked: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onReplayClicked() }
        ) {
            Icon(Icons.Rounded.Replay10, contentDescription = "Rewind")
        }
        IconButton(
            onClick = { onPlayClicked() },
            modifier = modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "Play/Pause",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.size(48.dp)
            )
        }

        IconButton(
            onClick = { onFastForwardClicked() }
        ) {
            Icon(Icons.Rounded.FastForward, contentDescription = "Forward")
        }
    }
}

fun formatTime(millis: Int): String {
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}


@Preview
@Composable
private fun PreviewReciterSurahRecitationScreen() {
    ReciterSurahRecitationScreen(
        state = SurahRecitationState(
            audioUrl = "https://example.com/audio.mp3",
            title = "Surah Name",
            currentSurah = SurahModel(
                name = "Surah Name",
                type = "Type",
                verses = emptyList(),
                totalVerses = 11,
                transliteration = "",
                id = 1
            )
        ), onBackClicked = {})
}