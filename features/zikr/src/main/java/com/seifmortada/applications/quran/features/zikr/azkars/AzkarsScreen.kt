package com.seifmortada.applications.quran.features.zikr.azkars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.domain.model.AzkarItemModel
import com.seifmortada.applications.quran.core.ui.R
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
        title = stringResource(R.string.azkars_title),
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

        Column(
            modifier = scaffoldModifier.padding(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                            text = azkarsList.size.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = stringResource(R.string.categories),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val totalAzkars = azkarsList.sumOf { it.array.size }
                        Text(
                            text = totalAzkars.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = stringResource(R.string.total_azkars),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(azkarsList) { zikr ->
                    EnhancedZikrCard(zikr, onZikrClicked)
                }
            }
        }
    }
}

@Composable
fun EnhancedZikrCard(
    zikr: AzkarModel,
    onZikrClicked: (AzkarModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onZikrClicked(zikr) },
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
            // Category icon in circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            zikr.category.contains("صباح", ignoreCase = true) ||
                                    zikr.category.contains("morning", ignoreCase = true) ->
                                MaterialTheme.colorScheme.tertiaryContainer

                            zikr.category.contains("مساء", ignoreCase = true) ||
                                    zikr.category.contains("evening", ignoreCase = true) ->
                                MaterialTheme.colorScheme.secondaryContainer

                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        zikr.category.contains("صباح", ignoreCase = true) ||
                                zikr.category.contains("مساء", ignoreCase = true) ||
                                zikr.category.contains("morning", ignoreCase = true) ||
                                zikr.category.contains(
                                    "evening",
                                    ignoreCase = true
                                ) -> Icons.Default.AccessTime

                        else -> Icons.Default.Book
                    },
                    contentDescription = null,
                    tint = when {
                        zikr.category.contains("صباح", ignoreCase = true) ||
                                zikr.category.contains("morning", ignoreCase = true) ->
                            MaterialTheme.colorScheme.onTertiaryContainer

                        zikr.category.contains("مساء", ignoreCase = true) ||
                                zikr.category.contains("evening", ignoreCase = true) ->
                            MaterialTheme.colorScheme.onSecondaryContainer

                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            // Zikr info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = zikr.category,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "${zikr.array.size} ${stringResource(R.string.azkars)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Show total recitation count
                val totalRecitations = zikr.array.sumOf { it.count }
                if (totalRecitations > 0) {
                    Text(
                        text = "${stringResource(R.string.total_recitations)}: $totalRecitations",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Completion indicator if needed
            if (zikr.array.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = zikr.array.size.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AzkarsScreenPreview() {
    val sampleAzkars = listOf(
        AzkarModel(
            array = listOf(
                AzkarItemModel(
                    audio = "",
                    count = 3,
                    filename = "",
                    id = 1,
                    text = "أَصْـبَحْنا وَأَصْـبَحَ المُـلْكُ لله"
                ),
                AzkarItemModel(
                    audio = "",
                    count = 7,
                    filename = "",
                    id = 2,
                    text = "الحمد لله رب العالمين"
                )
            ),
            audio = "",
            category = "أذكار الصباح",
            filename = "",
            id = 1
        ),
        AzkarModel(
            array = listOf(
                AzkarItemModel(
                    audio = "",
                    count = 3,
                    filename = "",
                    id = 3,
                    text = "أَمْسَيْنا وَأَمْسَى المُـلْكُ لله"
                )
            ),
            audio = "",
            category = "أذكار المساء",
            filename = "",
            id = 2
        )
    )
    
    AzkarsScreen(
        azkarsList = sampleAzkars,
        onBackClick = {},
        onZikrClicked = {},
        onSearchQueryChanged = {}
    )
}
