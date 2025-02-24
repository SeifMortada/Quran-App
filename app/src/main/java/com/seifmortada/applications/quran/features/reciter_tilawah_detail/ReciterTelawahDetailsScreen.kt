package com.seifmortada.applications.quran.features.reciter_tilawah_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
fun ReciterTelawahDetailsCore(
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
                title = "التلاوات",
                onBackClick = onBackClick
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        TelawahGrid(
            moshafList = reciter.moshaf,
            modifier = Modifier.padding(paddingValues),
            onTelawahClick = onTelawahClick
        )
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
        contentPadding = PaddingValues(mediumPadding)
    ) {
        items(moshafList) { telawah ->
            TelawahItem(telawah, onTelawahClick)
        }
    }
}

@Composable
fun TelawahItem(telawah: MoshafModel, onTelawahClick: (MoshafModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding)
            .clickable { onTelawahClick(telawah) }
            .size(70.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = telawah.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


private val ITEM_SPACING = 16.dp
private val PADDING = 12.dp
private val PADDING_SMALL = 8.dp


@Preview
@Composable
private fun ReciterTelawahDetailsScreenPreview() {
    ReciterTelawahDetailsScreen(
        reciter = ReciterModel(
            id = 1,
            name = "حفص عن عاصم",
            moshaf = listOf(
                MoshafModel(
                    1,
                    moshafType = 2,
                    name = "حفص عن عاصم",
                    server = "",
                    surahList = "",
                    surahTotal = 10
                ),
                MoshafModel(
                    2,
                    moshafType = 2,
                    name = " 2حفص عن عاصم",
                    server = "",
                    surahList = "",
                    surahTotal = 10
                ),

                MoshafModel(
                    3,
                    moshafType = 2,
                    name = " 2حفص عن عاصم",
                    server = "",
                    surahList = "",
                    surahTotal = 10
                ),

                MoshafModel(
                    4,
                    moshafType = 2,
                    name = " 2حفص عن عاصم",
                    server = "",
                    surahList = "",
                    surahTotal = 10
                ),
            ),
            date = "",
            letter = ""
        ),
        onBackClick = {},
        onTelawahClick = {}
    )
}