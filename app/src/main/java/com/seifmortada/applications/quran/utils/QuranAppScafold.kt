package com.seifmortada.applications.quran.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun QuranAppScaffold(
    modifier: Modifier = Modifier,
    title: String,
    isSearchable: Boolean = false,
    isSearchActive: Boolean = false,
    searchQuery: String = "",
    onBackClick: () -> Unit,
    onSearchBackClick: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    onSearchClick: (Boolean) -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (isSearchable && isSearchActive) {
                SearchToolbar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = onSearchQueryChanged,
                    onSearchTriggered = onSearchTriggered,
                    onBackClick = onSearchBackClick
                )
            } else if (isSearchable) {
                SearchTopAppBar(
                    title = title,
                    onBackClick = onBackClick,
                    onSearchClick = onSearchClick
                )
            } else {
                BackTopAppBar(
                    title = title,
                    onBackClick = onBackClick
                )
            }
        }
    ) { paddingValues ->
        Column {
            content(Modifier.padding(paddingValues))
        }
    }
}