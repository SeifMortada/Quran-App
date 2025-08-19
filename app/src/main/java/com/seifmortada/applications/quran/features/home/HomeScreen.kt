package com.seifmortada.applications.quran.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.main.MainItem
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme

@Composable
fun HomeRoute(
    onZikrClick: () -> Unit,
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    val zikr = stringResource(R.string.zikrs)
    val quran = stringResource(R.string.quran)
    val reciters = stringResource(R.string.quran_readers)
    val mainItems = remember {
        listOf(
            MainItem(zikr, R.drawable.ic_tasbih),
            MainItem(quran, R.drawable.ic_koran),
            MainItem(reciters, R.drawable.ic_imam)
        )
    }
    HomeScreen(
        mainItems = mainItems,
        onZikrClick = onZikrClick,
        onQuranClick = onQuranClick,
        onReciterClick = onReciterClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainItems: List<MainItem>,
    onZikrClick: () -> Unit,
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mainItems) { item ->
                MainItemRow(item, onZikrClick, onQuranClick, onReciterClick)
            }
        }
    }
}

@Composable
fun MainItemRow(
    item: MainItem,
    onZikrClick: () -> Unit,
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    val zikrsTitle = stringResource(R.string.zikrs)
    val quranTitle = stringResource(R.string.quran)
    val recitersTitle = stringResource(R.string.quran_readers)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                when (item.title) {
                    zikrsTitle -> onZikrClick()
                    quranTitle -> onQuranClick()
                    recitersTitle -> onReciterClick()
                }
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = item.title,
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    QuranAppTheme {
        HomeScreen(
            mainItems = listOf(
                MainItem(stringResource(R.string.zikrs), R.drawable.ic_tasbih),
                MainItem(stringResource(R.string.quran), R.drawable.ic_koran),
                MainItem(stringResource(R.string.quran_readers), R.drawable.ic_imam)
            ), {}, {}, {}
        )
    }
}
