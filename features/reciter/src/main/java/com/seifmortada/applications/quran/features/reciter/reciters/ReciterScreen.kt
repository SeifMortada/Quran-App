package com.seifmortada.applications.quran.features.reciter.reciters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.seifmortada.applications.quran.core.domain.model.MoshafModel
import com.seifmortada.applications.quran.core.domain.model.ReciterModel
import com.seifmortada.applications.quran.core.ui.R
import com.seifmortada.applications.quran.core.ui.composables.LanguagePreviews
import com.seifmortada.applications.quran.core.ui.composables.ThemePreviews
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.utils.SearchToolbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReciterRoute(
    onBackClick: () -> Unit,
    onReciterClick: (ReciterModel) -> Unit,
    viewModel: RecitersViewModel = koinViewModel()
) {
    val reciterState by viewModel.uiState.collectAsState()
    ReciterScreen(
        uiState = reciterState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onBackClick = onBackClick,
        onReciterClick = onReciterClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterScreen(
    uiState: ReciterScreenState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onReciterClick: (ReciterModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(uiState.searchQuery.isNotBlank()) }
    var searchQuery by remember(uiState.searchQuery) { 
        mutableStateOf(uiState.searchQuery) 
    }
    
    if (uiState.searchQuery != searchQuery) {
        searchQuery = uiState.searchQuery
        isSearch = uiState.searchQuery.isNotBlank()
    }
    
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isSearch,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    SearchToolbar(
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { query ->
                            searchQuery = query
                            onSearchQueryChanged(query)
                        },
                        onSearchTriggered = { isSearch = false },
                        onBackClick = { 
                            isSearch = false 
                            searchQuery = ""
                            onSearchQueryChanged("")
                        }
                    )
                }
            }
            
            AnimatedVisibility(
                visible = !isSearch,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = if (layoutDirection == LayoutDirection.Ltr) 
                                Arrangement.Start 
                            else 
                                Arrangement.End
                        ) {
                            if (layoutDirection == LayoutDirection.Ltr) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            
                            Column(
                                horizontalAlignment = if (layoutDirection == LayoutDirection.Ltr) 
                                    Alignment.Start 
                                else 
                                    Alignment.End
                            ) {
                                Text(
                                    text = stringResource(R.string.quran_readers),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (uiState.reciters.isNotEmpty() && !uiState.isLoading) {
                                    Text(
                                        text = "${uiState.reciters.size} ${stringResource(R.string.reciters_available)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            
                            if (layoutDirection == LayoutDirection.Rtl) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        androidx.compose.material3.IconButton(
                            onClick = {
                                if (uiState.searchQuery.isNotBlank()) {
                                    onSearchQueryChanged("")
                                }
                                onBackClick()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        androidx.compose.material3.IconButton(
                            onClick = { isSearch = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.error != null -> {
                    ErrorState(
                        errorMessage = uiState.error,
                        onRetryClick = { /* Add retry logic */ }
                    )
                }
                uiState.reciters.isEmpty() && uiState.searchQuery.isNotBlank() -> {
                    EmptySearchState(searchQuery = uiState.searchQuery)
                }
                uiState.reciters.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    ReciterList(
                        reciters = uiState.reciters,
                        onReciterClick = onReciterClick,
                        searchQuery = uiState.searchQuery
                    )
                }
            }
        }
    }
}

@Composable
fun ReciterList(
    reciters: List<ReciterModel>,
    onReciterClick: (ReciterModel) -> Unit,
    searchQuery: String
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (searchQuery.isNotBlank()) {
            item {
                SearchResultsHeader(
                    totalResults = reciters.size,
                    searchQuery = searchQuery
                )
            }
        }

        itemsIndexed(reciters, key = { _, reciter -> reciter.id }) { index, reciter ->
            EnhancedReciterCard(
                reciter = reciter,
                onReciterClick = onReciterClick,
                index = index
            )
        }
    }
}

@Composable
private fun SearchResultsHeader(
    totalResults: Int,
    searchQuery: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$totalResults ${stringResource(R.string.reciters_available)} \"$searchQuery\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun EnhancedReciterCard(
    reciter: ReciterModel,
    onReciterClick: (ReciterModel) -> Unit,
    index: Int
) {
    val layoutDirection = LocalLayoutDirection.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReciterClick(reciter) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (layoutDirection == LayoutDirection.Ltr)
                Arrangement.Start
            else
                Arrangement.End
        ) {
            // Avatar
            if (layoutDirection == LayoutDirection.Ltr) {
                ReciterAvatar(reciterName = reciter.name, index = index)
                Spacer(modifier = Modifier.width(16.dp))
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (layoutDirection == LayoutDirection.Ltr)
                    Alignment.Start
                else
                    Alignment.End
            ) {
                // Name
                Text(
                    text = reciter.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (layoutDirection == LayoutDirection.Ltr)
                        TextAlign.Start
                    else
                        TextAlign.End
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Recitation info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (layoutDirection == LayoutDirection.Ltr)
                        Arrangement.Start
                    else
                        Arrangement.End
                ) {
                    if (layoutDirection == LayoutDirection.Rtl) {
                        Text(
                            text = stringResource(R.string.select_riwayat_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Recitations count badge
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${reciter.moshaf.size}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    if (layoutDirection == LayoutDirection.Ltr) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.select_riwayat_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Additional info if available
                if (reciter.date.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reciter.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = if (layoutDirection == LayoutDirection.Ltr)
                            TextAlign.Start
                        else
                            TextAlign.End
                    )
                }
            }

            // Avatar for RTL
            if (layoutDirection == LayoutDirection.Rtl) {
                Spacer(modifier = Modifier.width(16.dp))
                ReciterAvatar(reciterName = reciter.name, index = index)
            }

            // Action indicator
            Icon(
                imageVector = if (layoutDirection == LayoutDirection.Ltr)
                    Icons.Default.ChevronRight
                else
                    Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ReciterAvatar(
    reciterName: String,
    index: Int
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )

    val selectedGradient = listOf(
        gradientColors[index % gradientColors.size],
        gradientColors[(index + 1) % gradientColors.size]
    )

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(selectedGradient)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = reciterName.take(2).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = stringResource(R.string.loading_reciters),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorState(
    errorMessage: String,
    onRetryClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = stringResource(R.string.error_icon_content_description),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = stringResource(R.string.error_occurred, errorMessage),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onRetryClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RecordVoiceOver,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(72.dp)
            )

            Text(
                text = stringResource(R.string.no_reciters_found),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.no_reciters_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptySearchState(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = stringResource(R.string.no_results_icon_content_description),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.no_reciters_matching_search, searchQuery),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@LanguagePreviews
@ThemePreviews
@Composable
private fun ReciterScreenPreview() {
    QuranAppTheme {
        ReciterScreen(
            uiState = ReciterScreenState(
                reciters = listOf(
                    ReciterModel(
                        name = "الشيخ عبد الرحمن السديس",
                        date = "1962",
                        id = 1,
                        letter = "ا",
                        moshaf = listOf(
                            MoshafModel(
                                id = 1,
                                moshafType = 0,
                                name = "حفص عن عاصم",
                                server = "",
                                surahList = "",
                                surahTotal = 114
                            ),
                            MoshafModel(
                                id = 2,
                                moshafType = 1,
                                name = "ورش عن نافع",
                                server = "",
                                surahList = "",
                                surahTotal = 114
                            )
                        )
                    ),
                    ReciterModel(
                        name = "الشيخ سعد الغامدي",
                        date = "1967",
                        id = 2,
                        letter = "س",
                        moshaf = listOf(
                            MoshafModel(
                                id = 3,
                                moshafType = 0,
                                name = "حفص عن عاصم",
                                server = "",
                                surahList = "",
                                surahTotal = 114
                            )
                        )
                    ),
                    ReciterModel(
                        name = "الشيخ محمد صديق المنشاوي",
                        date = "1920-1969",
                        id = 3,
                        letter = "م",
                        moshaf = listOf(
                            MoshafModel(
                                id = 4,
                                moshafType = 0,
                                name = "حفص عن عاصم",
                                server = "",
                                surahList = "",
                                surahTotal = 114
                            ),
                            MoshafModel(
                                id = 5,
                                moshafType = 1,
                                name = "ورش عن نافع",
                                server = "",
                                surahList = "",
                                surahTotal = 114
                            ),
                            MoshafModel(
                                id = 6,
                                moshafType = 2,
                                name = "قالون عن نافع",
                                server = "",
                                surahList = "",
                                surahTotal = 114
                            )
                        )
                    )
                ),
                isLoading = false,
                searchQuery = "",
                error = null
            ),
            onSearchQueryChanged = {},
            onBackClick = {},
            onReciterClick = {}
        )
    }
}
