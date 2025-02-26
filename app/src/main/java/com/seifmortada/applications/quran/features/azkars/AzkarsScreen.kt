package com.seifmortada.applications.quran.features.azkars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AzkarModel
import com.seifmortada.applications.quran.utils.QuranAppScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun AzkarsRoute(
    onBackClick: () -> Unit,
    onZikrClicked: (AzkarModel) -> Unit,
    viewModel: AzkarViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    AzkarsScreen(
        azkarsList = state,
        onBackClick = onBackClick,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onZikrClicked = onZikrClicked
    )
}

@Composable
fun AzkarsScreen(
    azkarsList: List<AzkarModel>,
    onBackClick: () -> Unit = {},
    onZikrClicked: (AzkarModel) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    QuranAppScaffold(
        modifier = modifier,
        title = "الأذكار",
        onBackClick = onBackClick,
        isSearchable = true,
        isSearchActive = isSearch,
        onSearchClick = { isSearch = it },
        searchQuery = searchQuery,
        onSearchQueryChanged = {
            searchQuery = it
            onSearchQueryChanged(it)
        },
        onSearchTriggered = { isSearch = false },
        onSearchBackClick = { isSearch = false }
    ) { scaffoldModifier ->
        LazyColumn(modifier = scaffoldModifier) {
            items(azkarsList) { zikr ->
                ZikrItem(zikr, onZikrClicked)
            }
        }
    }
}

@Composable
fun ZikrItem(zikr: AzkarModel, onZikrClicked: (AzkarModel) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onZikrClicked(zikr) },
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
                text = zikr.category,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    AzkarsScreen(
        emptyList(),
        {},
        {},
        {}
    )
}