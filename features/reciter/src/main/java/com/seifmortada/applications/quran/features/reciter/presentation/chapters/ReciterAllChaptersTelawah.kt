package com.seifmortada.applications.quran.features.reciter.presentation.chapters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import com.seifmortada.applications.quran.core.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.core.domain.model.MoshafModel
import com.seifmortada.applications.quran.core.domain.model.SurahModel
import com.seifmortada.applications.quran.core.ui.composables.ForceRightOrLeft
import com.seifmortada.applications.quran.core.ui.composables.LanguagePreviews
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReciterAllSurahsRoute(
    onBackClicked: () -> Unit,
    availableSurahsWithThisTelawah: MoshafModel,
    onSurahClicked: (SurahMoshafReciter) -> Unit,
    viewModel: ReciterAllSurahsViewModel = koinViewModel()
) {

    LaunchedEffect(availableSurahsWithThisTelawah) {
        viewModel.filterSurahsWithThisTelawah(availableSurahsWithThisTelawah)
    }
    val surahs by viewModel.filteredSurahs.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    ReciterAllSurahsScreen(
        moshaf = availableSurahsWithThisTelawah,
        surahs = surahs,
        searchQuery = searchQuery,
        onSurahClicked = onSurahClicked,
        onBackClicked = onBackClicked,
        onSearchQuery = { viewModel.updateSearchQuery(query = it) }
    )

}

@Composable
fun ReciterAllSurahsScreen(
    moshaf: MoshafModel,
    surahs: List<SurahModel>,
    searchQuery: String,
    onSurahClicked: (SurahMoshafReciter) -> Unit,
    onBackClicked: () -> Unit,
    onSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sync local state with ViewModel state
    var isSearch by remember(searchQuery) { mutableStateOf(searchQuery.isNotBlank()) }
    var localSearchQuery by remember(searchQuery) { mutableStateOf(searchQuery) }

    // Update local state when ViewModel search query changes
    if (searchQuery != localSearchQuery) {
        localSearchQuery = searchQuery
        isSearch = searchQuery.isNotBlank()
    }

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
                        searchQuery = localSearchQuery,
                        onSearchQueryChanged = {
                            localSearchQuery = it
                            onSearchQuery(it)
                        },
                        onSearchTriggered = { isSearch = false },
                        onBackClick = {
                            isSearch = false
                            localSearchQuery = ""
                            onSearchQuery("")
                        }
                    )
                }
            }
            AnimatedVisibility(
                visible = !isSearch,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                SearchTopAppBar(
                    title = "السور المتاحة لهذه التلاوة",
                    onBackClick = {
                        // Clear search when navigating back
                        if (searchQuery.isNotBlank()) {
                            onSearchQuery("")
                        }
                        onBackClicked()
                    },
                    onSearchClick = { isSearch = it }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues)
        ) {
            items(surahs) { surah ->
                SurahItem(
                    surah = surah,
                    moshaf = moshaf,
                    onSurahClicked = onSurahClicked
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun SurahItem(
    surah: SurahModel,
    moshaf: MoshafModel,
    onSurahClicked: (SurahMoshafReciter) -> Unit
) {
    ForceRightOrLeft(forceRight = false) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSurahClicked(SurahMoshafReciter(moshaf, surah.id)) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Surah number badge
            Text(
                text = "﴾ ${surah.id} ﴿",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = surah.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = if (surah.type == "meccan") "مكية • ${surah.totalVerses} آية"
                    else "مدنية • ${surah.totalVerses} آية",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@LanguagePreviews
@Composable
private fun ReciterAllSurahsScreenPreview() {
    QuranAppTheme {
        ReciterAllSurahsScreen(
            surahs = listOf(
                SurahModel(
                    1,
                    "الفاتحة",
                    totalVerses = 7,
                    transliteration = "Al-Fatihah",
                    type = "meccan",
                    verses = emptyList()
                ),
                SurahModel(
                    2,
                    "البقرة",
                    totalVerses = 286,
                    transliteration = "Al-Baqarah",
                    type = "medinan",
                    verses = emptyList()
                )
            ),
            onSearchQuery = { },
            onBackClicked = {},
            moshaf = MoshafModel(1, 2, "", "", "", 20),
            searchQuery = "",
            onSurahClicked = {}
        )
    }
}

