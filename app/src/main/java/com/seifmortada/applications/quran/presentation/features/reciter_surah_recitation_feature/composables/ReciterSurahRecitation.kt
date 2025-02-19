package com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature.SurahRecitationViewModel
import org.koin.androidx.compose.koinViewModel
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.NetworkResult
import com.seifmortada.applications.quran.presentation.common.composables.AppTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReciterSurahRecitationCore(
    onBackClicked: () -> Unit,
    viewModel: SurahRecitationViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val surahRecitation by viewModel.surahRecitationResponse.observeAsState()
    ReciterSurahRecitationScreen(
        surahRecitation = surahRecitation ?: NetworkResult.Loading,
        onBackClicked = onBackClicked,
        modifier = modifier
    )
}

@Composable
fun ReciterSurahRecitationScreen(
    surahRecitation: NetworkResult<String>,
    onBackClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    Scaffold(
        topBar = {
            AppTopAppBar(
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
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            when (surahRecitation) {
                is NetworkResult.Success -> {
                    AudioPlayer(title = "Title", audioUrl = surahRecitation.data, mediaPlayer = mediaPlayer)
                }

                is NetworkResult.Error -> {
                    ShowErrorMessage(errorMessage = surahRecitation.errorMessage)
                }

                else -> {
                    CircularProgressIndicator()
                }
            }
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
    var currentPosition by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(audioUrl) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepare()
        duration = mediaPlayer.duration
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (mediaPlayer.isPlaying) {
                currentPosition = mediaPlayer.currentPosition
                delay(500)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)


        Slider(
            value = currentPosition.toFloat(),
            onValueChange = {
                mediaPlayer.seekTo(it.toInt())
                currentPosition = it.toInt()
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { mediaPlayer.seekTo((currentPosition - 10000).coerceAtLeast(0)) }) {
                Icon(Icons.Filled.Clear, contentDescription = "Rewind")
            }

            IconButton(
                onClick = {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        isPlaying = false
                    } else {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                },
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Clear else Icons.Filled.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            IconButton(onClick = { mediaPlayer.seekTo((currentPosition + 10000).coerceAtMost(duration)) }) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Forward")
            }
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
    ReciterSurahRecitationScreen(surahRecitation = NetworkResult.Success(""), onBackClicked = {})
}