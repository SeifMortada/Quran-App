package com.seifmortada.applications.quran.features.reciter_tilawah_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.seifmortada.applications.quran.utils.BackTopAppBar
import com.seifmortada.applications.quran.utils.mediumPadding
import com.seifmortada.applications.quran.utils.smallPadding

@Composable
fun ReciterTelawahDetailsRoute(
    onBackClick: () -> Unit,
    onTelawahClick: (MoshafModel) -> Unit,
    reciter: ReciterModel
) {
    ReciterTelawahDetailsScreen(
        reciter = reciter,
        onBackClick = onBackClick,
        onTelawahClick = onTelawahClick
    )
}

@Composable
fun ReciterTelawahDetailsScreen(
    reciter: ReciterModel,
    onBackClick: () -> Unit,
    onTelawahClick: (MoshafModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = "تلاوات ${reciter.name}",
                onBackClick = onBackClick
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        if (reciter.moshaf.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "لا توجد تلاوات متاحة",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            TelawahGrid(
                moshafList = reciter.moshaf,
                modifier = Modifier.padding(paddingValues),
                onTelawahClick = onTelawahClick
            )
        }
    }
}

@Composable
fun TelawahGrid(
    moshafList: List<MoshafModel>,
    onTelawahClick: (MoshafModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(mediumPadding),
        horizontalArrangement = Arrangement.spacedBy(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(mediumPadding)
    ) {
        items(moshafList) { telawah ->
            TelawahItem(telawah, onTelawahClick)
        }
    }
}

@Composable
fun TelawahItem(
    telawah: MoshafModel,
    onTelawahClick: (MoshafModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f) // tile shape instead of fixed dp
            .clickable { onTelawahClick(telawah) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LibraryMusic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = telawah.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "عدد السور: ${telawah.surahTotal}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReciterTelawahDetailsScreenPreview() {
    ReciterTelawahDetailsScreen(
        reciter = ReciterModel(
            id = 1,
            name = "الشيخ مشاري العفاسي",
            moshaf = listOf(
                MoshafModel(
                    id = 1,
                    moshafType = 1,
                    name = "حفص عن عاصم",
                    server = "",
                    surahList = "",
                    surahTotal = 114
                ),
                MoshafModel(
                    id = 2,
                    moshafType = 2,
                    name = "ورش عن نافع",
                    server = "",
                    surahList = "",
                    surahTotal = 114
                ),
                MoshafModel(
                    id = 3,
                    moshafType = 3,
                    name = "قالون عن نافع",
                    server = "",
                    surahList = "",
                    surahTotal = 114
                )
            ),
            date = "",
            letter = ""
        ),
        onBackClick = {},
        onTelawahClick = {}
    )
}
