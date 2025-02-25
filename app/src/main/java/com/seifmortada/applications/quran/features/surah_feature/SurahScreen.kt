package com.seifmortada.applications.quran.features.surah_feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        onSearchQueryChanged = viewModel::onSearchQueryChanged
    )
}

@Composable
fun SurahScreen(
    state: SurahState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
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
                state.loading -> CircularProgressIndicator()
                state.error != null -> ShowErrorMessage(errorMessage = state.error)
                state.surah != null -> LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        SurahDisplay(surah = state.surah)
                    }
                }
            }
        }
    }
}
@Composable
private fun SurahDisplay(surah: SurahModel) {
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

            // Replacing LazyColumn with Column
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                surah.verses.forEach { verse ->
                    AyahItem(verse)
                }
            }
        }
    }
}



@Composable
private fun AyahItem(verse: VerseModel) {
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
    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
}
