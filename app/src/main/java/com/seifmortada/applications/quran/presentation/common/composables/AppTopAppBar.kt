package com.seifmortada.applications.quran.presentation.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    onSearchClick:(Boolean)->Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.md_theme_primary)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = {onSearchClick(true)}) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
        TopAppBar(
            modifier = Modifier.wrapContentSize(),
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.md_theme_onPrimary),
                    modifier = Modifier.padding(start = 20.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.md_theme_primary),
                scrolledContainerColor = Color.Transparent,
            ),
            navigationIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = onBackClick
                        )
                        .size(25.dp)
                )
            },
            actions = actions
        )
    }
}

    @Preview
    @Composable
    private fun AppTopAppBarPreview() {
        AppTopAppBar(title = "", onBackClick = {},{})

    }
