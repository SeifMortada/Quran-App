package com.seifmortada.applications.quran.features.surah

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SurahModel
import com.example.domain.model.VerseModel
import com.seifmortada.applications.quran.features.reciter_tilawah_recitation.ShowErrorMessage
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SurahRoute(
    surahId: Int,
    onBackClick: () -> Unit,
    viewModel: SurahViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(surahId) {
        viewModel.getSurahById(surahId)
    }

    SurahScreen(
        onBackClick = onBackClick,
        state = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onAudioStart = viewModel::getAyahRecitation,
        onAudioStop = viewModel::stopAudio
    )
}

@Composable
fun SurahScreen(
    state: SurahUiState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onAudioStart: (String, String, Int) -> Unit,
    onAudioStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val mediaPlayer = remember { MediaPlayer() }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    Scaffold(
        topBar = {
            if (isSearch) {
                SearchToolbar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                    },
                    onSearchTriggered = { isSearch = false },
                    onBackClick = { isSearch = false }
                )
            } else {
                SearchTopAppBar(
                    title = state.surah?.name ?: "Surah",
                    onBackClick = onBackClick,
                    onSearchClick = { isSearch = it }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.userMessage != null -> ShowErrorMessage(errorMessage = state.userMessage)
                state.surah != null -> LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        SurahDisplay(
                            uiState = state,
                            onAudioStop = onAudioStop,
                            onAudioStart = onAudioStart
                        )
                    }
                }

                state.surahAudioUrl != null -> playAudio(state.surahAudioUrl, mediaPlayer)
            }
        }
    }
}

fun playAudio(surahAudioUrl: String, mediaPlayer: MediaPlayer) {
    try {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(surahAudioUrl)
        mediaPlayer.prepare()
        mediaPlayer.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
private fun SurahDisplay(
    uiState: SurahUiState,
    onAudioStop: () -> Unit,
    onAudioStart: (String, String, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.surah?.name ?: "",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "(${uiState.surah?.type ?: ""})",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                uiState.surah?.verses?.forEach { verse ->
                    AyahItem(
                        verse = verse,
                        isPlaying = uiState.playingAyahId == verse.id,
                        onPlayPauseClick = {
                            if (uiState.playingAyahId == verse.id) {
                                onAudioStop()
                            } else {
                                onAudioStart(
                                    uiState.surah.id.toString(),
                                    verse.surahId.toString(),
                                    verse.id
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AyahItem(verse: VerseModel, isPlaying: Boolean, onPlayPauseClick: (VerseModel) -> Unit) {
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
            Text(
                text = "(${verse.id})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onPlayPauseClick(verse) }
                        .padding(8.dp)
                        .width(32.dp)
                        .height(32.dp)
                )
            }
        }
    }
}


@Composable
fun AyahAudioPlayer(modifier: Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    isPlaying = !isPlaying
                }
        )


    }
}

@Preview
@Composable
private fun SurahScreenPreview() {

}
