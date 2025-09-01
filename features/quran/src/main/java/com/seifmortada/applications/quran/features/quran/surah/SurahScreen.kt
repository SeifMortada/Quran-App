package com.seifmortada.applications.quran.features.quran.surah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.domain.model.VerseModel
import org.koin.androidx.compose.koinViewModel
import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import com.seifmortada.applications.quran.core.ui.R
import com.seifmortada.applications.quran.core.ui.composables.ShowErrorMessage
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import kotlinx.coroutines.launch

@Composable
fun SurahRoute(
    surahId: Int,
    onBackClick: () -> Unit,
    viewModel: SurahViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var playingAyah by remember { mutableStateOf<Pair<Int, Int>?>(null) } // Pair<surahId, ayahId>
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun playAyahAudio(surahNumber: Int, ayahNumber: Int, audioUrl: String) {
        // Stop previous playback
        mediaPlayer?.release()
        mediaPlayer = null

        val player = MediaPlayer()
        try {
            player.setDataSource(audioUrl)
            player.prepareAsync()
            player.setOnPreparedListener {
                it.start()
                playingAyah = Pair(surahNumber, ayahNumber)
            }
            player.setOnCompletionListener {
                playingAyah = null
                it.release()
                mediaPlayer = null
            }
            mediaPlayer = player
        } catch (e: Exception) {
            playingAyah = null
            player.release()
            mediaPlayer = null
        }
    }

    fun stopAyahAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        playingAyah = null
    }

    // Clean up on dispose
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    LaunchedEffect(surahId) {
        viewModel.getSurahById(surahId)
        stopAyahAudio()
    }

    SurahScreen(
        onBackClick = onBackClick,
        state = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onAyahPlayClicked = { surahNumber, ayahNumber ->
            // If same ayah is playing, pause/stop
            if (playingAyah == Pair(surahNumber, ayahNumber)) {
                stopAyahAudio()
            } else {
                coroutineScope.launch {
                    val url = viewModel.getAyahAudioUrl(surahNumber, ayahNumber)
                    if (url.isNullOrEmpty()) {
                        stopAyahAudio()
                        return@launch
                    }
                    playAyahAudio(surahNumber, ayahNumber, url)
                }
            }
        },
        currentlyPlayingAyah = playingAyah
    )
}

@Composable
fun SurahScreen(
    state: SurahUiState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onAyahPlayClicked: (Int, Int) -> Unit = { _, _ -> },
    currentlyPlayingAyah: Pair<Int, Int>? = null,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

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
                    title = state.surah?.name ?: stringResource(R.string.surah_recitation),
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
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                state.userMessage != null -> {
                    ShowErrorMessage(
                        errorMessage = state.userMessage,
                        onRetry = {}
                    )
                }

                state.surah != null -> {
                    EnhancedSurahDisplay(
                        surah = state.surah,
                        currentlyPlayingAyah = currentlyPlayingAyah,
                        onAyahPlayClicked = { ayahId ->
                            onAyahPlayClicked(state.surah.id, ayahId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedSurahDisplay(
    surah: SurahModel,
    currentlyPlayingAyah: Pair<Int, Int>?,
    onAyahPlayClicked: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = surah.id.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = surah.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    if (surah.transliteration.isNotEmpty()) {
                        Text(
                            text = surah.transliteration,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (surah.type.contains("Meccan", ignoreCase = true))
                                    Icons.Default.Place else Icons.Default.Book,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = surah.type,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Text(
                            text = "${surah.totalVerses} ${stringResource(R.string.verses)}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        if (surah.id != 9) {
            item {
                BismillahCard()
            }
        }

        // Verses
        items(
            items = surah.verses,
            key = { verse -> verse.id }
        ) { verse ->
            EnhancedAyahCard(
                verse = verse,
                surahNumber = surah.id,
                onPlayClicked = { onAyahPlayClicked(verse.id) },
                isPlaying = currentlyPlayingAyah?.second == verse.id
            )
        }
    }
}

@Composable
private fun BismillahCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Text(
            text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Composable
private fun EnhancedAyahCard(
    verse: VerseModel,
    surahNumber: Int,
    onPlayClicked: () -> Unit,
    isPlaying: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Ayah text
            Text(
                text = verse.text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp,
                    lineHeight = 36.sp
                ),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom row with verse number and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Play/Pause button
                    IconButton(
                        onClick = onPlayClicked,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Bookmark button
                    IconButton(
                        onClick = { /* TODO: Implement bookmark */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = { /* TODO: Implement share */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Verse number in decorative circle
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = verse.id.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SurahScreenPreview() {
    SurahScreen(
        state = SurahUiState(
            surah = SurahModel(
                1,
                "الفاتحة",
                7,
                "Meccan",
                "Al-Fatihah",
                listOf(
                    VerseModel(1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ", 1),
                    VerseModel(2, "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ", 2)
                )
            ),
        ),
        onSearchQueryChanged = {},
        onBackClick = {}
    )
}
