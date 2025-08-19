package com.seifmortada.applications.quran.features.reciters

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ReciterModel
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.ui.composables.LanguagePreviews
import com.seifmortada.applications.quran.core.ui.composables.ThemePreviews
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar
import com.seifmortada.applications.quran.utils.mediumPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReciterRoute(
    onBackClick: () -> Unit,
    onReciterClick: (ReciterModel) -> Unit,
    viewModel: RecitersViewModel = koinViewModel()
) {
    val reciterState by viewModel.uiState.collectAsState()
    ReciterScreen(reciterState, viewModel::onSearchQueryChanged, onBackClick, onReciterClick)
}

@Composable
fun ReciterScreen(
    uiState: ReciterScreenState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onReciterClick: (ReciterModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
        Scaffold(topBar = {
            if (isSearch) {
                SearchToolbar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                    },
                    onSearchTriggered = { isSearch = false },
                    onBackClick = { isSearch = false }
                )
            } else {
                SearchTopAppBar(
                    title = stringResource(R.string.quran_readers),
                    onBackClick = onBackClick,
                    onSearchClick = { isSearch = it }
                )
            }
        })
        { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isLoading) {
                    LoadingScreen()
                }
                if (uiState.error != null) {
                    ErrorScreen(uiState.error)
                }
                if (uiState.reciters.isNotEmpty()) {
                    ReciterList(reciters = uiState.reciters, onReciterClick)
                }

            }
        }
    }


@Composable
fun ReciterList(reciters: List<ReciterModel>, onReciterClick: (ReciterModel) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(mediumPadding)) {
        items(reciters, key = { it.id }) {
            ReciterCard(it, onReciterClick)
        }
    }
}

@Composable
fun ReciterCard(reciter: ReciterModel, onReciterClick: (ReciterModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onReciterClick(reciter) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = reciter.name.first().toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = reciter.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${reciter.moshaf.size} رواية",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "اضغط لاختيار الروايات",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        }
    }
}


@Composable
fun ErrorScreen(errorMessage: String) {
    Box {
        Text(text = "Error , $errorMessage")
    }
}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator()
}
@LanguagePreviews
@ThemePreviews
@Composable
private fun ReciterScreenPreview() {
    QuranAppTheme {

        ReciterScreen(
            ReciterScreenState(
                listOf(
                    ReciterModel(
                        name = "Name1",
                        date = "",
                        id = 1,
                        letter = "",
                        moshaf = emptyList()
                    ),
                    ReciterModel(
                        name = "Name2",
                        date = "",
                        id = 2,
                        letter = "",
                        moshaf = emptyList()
                    ),
                    ReciterModel(
                        name = "Name3",
                        date = "",
                        id = 3,
                        letter = "",
                        moshaf = emptyList()
                    )
                ), false, "", null
            ), {}, {}, {})
    }
}