package com.seifmortada.applications.quran.features.quran_chapters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SurahModel
import com.seifmortada.applications.quran.R
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
    Scaffold(topBar = {
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
    }) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (chaptersState.isNotEmpty()) {
                LazyColumn {
                    items(chaptersState) { chapter ->
                        ChapterCard(chapter, onChapterClick)
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterCard(chapter: SurahModel, onChapterClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChapterClick(chapter.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = chapter.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
private fun QuranChaptersPreview() {
QuranChaptersScreen(
    onBackClick = {},
    onSearchQueryChanged = {},
    chaptersState = listOf(),
    onChapterClick = {}

)
}