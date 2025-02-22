package com.seifmortada.applications.quran.presentation.features.reciter_telawahs_feature

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MoshafModel
import com.example.domain.model.SurahModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import com.seifmortada.applications.quran.presentation.common.composables.SearchToolbar
import com.seifmortada.applications.quran.presentation.features.reciter_surah_recitation_feature.ReciterSurahRecitationScreen
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReciterAllSurahsCore(
    onBackClicked: () -> Unit,
    availableSurahsWithThisTelawah: MoshafModel,
    viewModel: ReciterAllSurahsViewModel = koinViewModel()
) {

    LaunchedEffect(availableSurahsWithThisTelawah) {
        viewModel.filterSurahsWithThisTelawah(availableSurahsWithThisTelawah)
    }
    val surahs by viewModel.filteredSurahs.collectAsState()
    ReciterAllSurahsScreen(availableSurahsWithThisTelawah, surahs,
        onBackClicked = onBackClicked,
        onSearchQuery = { viewModel.updateSearchQuery(query = it) })

}

@Composable
fun ReciterAllSurahsScreen(
    moshaf: MoshafModel,
    surahs: List<SurahModel>,
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
                    onBackClick = onBackClicked
                )
            } else {
                SearchTopAppBar(
                    title = "السور المتاحة لهذة التلاوة",
                    onBackClick = {},
                    onSearchClick = { isSearch = it }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(modifier = modifier.padding(paddingValues)) {
            items(surahs) { surah ->
                SurahItem(surah = surah, moshaf = moshaf) {}
            }
        }
    }
}

@Composable
fun SurahItem(
    surah: SurahModel,
    moshaf: MoshafModel,
    onClick: (SurahMoshafReciter) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(SurahMoshafReciter(moshaf, surah.id)) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = surah.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (surah.type == "meccan") "مكية - ${surah.totalVerses} آية"
                else "مدنية - ${surah.totalVerses} آية",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "﴾ ${surah.id} ﴿",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

