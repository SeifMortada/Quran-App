package com.seifmortada.applications.quran.features.quran.chapters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.domain.model.VerseModel
import com.seifmortada.applications.quran.core.ui.R
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuranChaptersRoute(
    onBackClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    viewModel: QuranChaptersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    QuranChaptersScreen(
        onBackClick = onBackClick,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        chaptersState = uiState,
        onChapterClick = onChapterClick
    )
}

@Composable
fun QuranChaptersScreen(
    chaptersState: List<SurahModel>,
    onBackClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onChapterClick: (Int) -> Unit,
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
                    title = stringResource(R.string.quran),
                    onBackClick = onBackClick,
                    onSearchClick = { isSearch = it }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header with total info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "114",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = stringResource(R.string.surahs),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "6,236",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = stringResource(R.string.verses),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            if (chaptersState.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = chaptersState,
                        key = { chapter -> chapter.id }
                    ) { chapter ->
                        EnhancedChapterCard(chapter, onChapterClick)
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedChapterCard(chapter: SurahModel, onChapterClick: (Int) -> Unit) {
    val isMeccan = remember(chapter.type) {
        chapter.type.contains("Meccan", ignoreCase = true)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChapterClick(chapter.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Surah number in circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chapter.id.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Surah info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = chapter.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = chapter.transliteration.ifEmpty { chapter.type },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Type and verses count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isMeccan) Icons.Default.Place else Icons.Default.Book,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = chapter.type,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Text(
                        text = "${chapter.totalVerses} ${stringResource(R.string.verses)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuranChaptersPreview() {
    QuranChaptersScreen(
        onBackClick = {},
        onSearchQueryChanged = {},
        chaptersState = listOf(
            SurahModel(
                id = 1,
                name = "الفاتحة",
                totalVerses = 7,
                type = "Meccan",
                transliteration = "Al-Fatihah",
                verses = listOf(
                    VerseModel(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", 1)
                )
            ),
            SurahModel(
                id = 2,
                name = "البقرة",
                totalVerses = 286,
                type = "Medinan",
                transliteration = "Al-Baqarah",
                verses = emptyList()
            )
        ),
        onChapterClick = {}
    )
}
