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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.main.MainItem
import com.seifmortada.applications.quran.R

@Composable
fun HomeRoute(
    onZikrClick: () -> Unit,
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    val mainItems = remember {
        listOf(
            MainItem("Zikrs", R.drawable.ic_tasbih),
            MainItem("Quran", R.drawable.ic_koran),
            MainItem("Quran Readers", R.drawable.ic_imam)
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
    val zikrsTitle = "Zikrs"
    val quranTitle = "Quran"
    val recitersTitle = "Quran Readers"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    when (item.title) {
                        zikrsTitle -> onZikrClick()
                        quranTitle -> onQuranClick()
                        recitersTitle -> onReciterClick()
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.title,
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        mainItems = listOf(
            MainItem(stringResource(R.string.zikrs), R.drawable.ic_tasbih),
            MainItem(stringResource(R.string.quran), R.drawable.ic_koran),
            MainItem(stringResource(R.string.quran_readers), R.drawable.ic_imam)
        ), {}, {}, {}
    )

}
