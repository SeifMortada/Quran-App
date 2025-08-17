package com.seifmortada.applications.quran.features.reciter_tilawah_chapters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MoshafModel
import com.example.domain.model.SurahModel
import com.example.domain.model.VerseModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
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
    ReciterAllSurahsScreen(
        moshaf = availableSurahsWithThisTelawah,
        surahs = surahs,
        onBackClicked = onBackClicked,
        onSurahClicked = onSurahClicked,
        onSearchQuery = { viewModel.updateSearchQuery(query = it) }
    )

}

@Composable
fun ReciterAllSurahsScreen(
    moshaf: MoshafModel,
    surahs: List<SurahModel>,
    onSurahClicked: (SurahMoshafReciter) -> Unit,
    onBackClicked: () -> Unit,
    onSearchQuery: (String) -> Unit,
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
                        onSearchQuery(it)
                    },
                    onSearchTriggered = { isSearch = false },
                    onBackClick = { isSearch = false }
                )
            } else {
                SearchTopAppBar(
                    title = "السور المتاحة لهذه التلاوة",
                    onBackClick = onBackClicked,
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

@Preview
@Composable
private fun ReciterAllSurahsScreenPreview() {
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
        onSurahClicked = {}
    )
}

