package com.seifmortada.applications.quran.features.zikr.zikr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.core.domain.model.AzkarItemModel
import com.seifmortada.applications.quran.core.domain.model.AzkarModel
import com.seifmortada.applications.quran.core.ui.R
import com.seifmortada.applications.quran.core.ui.utils.FunctionsUtils
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar

@Composable
fun ZikrRoute(zikr: AzkarModel, onBackClicked: () -> Unit) {
    ZikrScreen(
        zikr = zikr,
        onBackButtonClicked = onBackClicked
    )
}

@Composable
fun ZikrScreen(
    zikr: AzkarModel,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredAzkars = zikr.array.filter {
        FunctionsUtils.normalizeTextForFiltering(it.text)
            .contains(searchQuery, ignoreCase = true)
    }
    var isSearch by remember { mutableStateOf(false) }

    // Track completion state
    var completedCounts by remember {
        mutableStateOf(filteredAzkars.associateWith { 0 })
    }

    val totalRecitations = filteredAzkars.sumOf { it.count }
    val completedRecitations = completedCounts.values.sum()
    val progressPercentage = if (totalRecitations > 0) {
        (completedRecitations.toFloat() / totalRecitations) * 100
    } else 0f

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isSearch) {
                SearchToolbar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { newQuery -> searchQuery = newQuery },
                    onSearchTriggered = { isSearch = false },
                    onBackClick = { isSearch = false }
                )
            } else {
                SearchTopAppBar(
                    title = zikr.category,
                    onBackClick = onBackButtonClicked,
                    onSearchClick = { isSearch = it }
                )
            }
        },
        floatingActionButton = {
            if (completedRecitations > 0) {
                FloatingActionButton(
                    onClick = {
                        completedCounts = filteredAzkars.associateWith { 0 }
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.reset_progress)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Progress Header
            if (totalRecitations > 0) {
                ProgressHeader(
                    completedRecitations = completedRecitations,
                    totalRecitations = totalRecitations,
                    progressPercentage = progressPercentage
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredAzkars) { zikrItem ->
                    val completedCount = completedCounts[zikrItem] ?: 0
                    EnhancedAzkarCard(
                        zikrItem = zikrItem,
                        completedCount = completedCount,
                        onZikrTapped = {
                            if (completedCount < zikrItem.count) {
                                completedCounts = completedCounts.toMutableMap().apply {
                                    this[zikrItem] = (this[zikrItem] ?: 0) + 1
                                }
                            }
                        },
                        onResetClicked = {
                            completedCounts = completedCounts.toMutableMap().apply {
                                this[zikrItem] = 0
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressHeader(
    completedRecitations: Int,
    totalRecitations: Int,
    progressPercentage: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.progress),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$completedRecitations / $totalRecitations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progressPercentage / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${progressPercentage.toInt()}% ${stringResource(R.string.completed)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EnhancedAzkarCard(
    zikrItem: AzkarItemModel,
    completedCount: Int,
    onZikrTapped: () -> Unit,
    onResetClicked: () -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    val remainingCount = (zikrItem.count - completedCount).coerceAtLeast(0)
    val isCompleted = remainingCount == 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isCompleted) {
                vibrate(vibrator)
                onZikrTapped()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(if (isCompleted) 2.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Zikr Text
            Text(
                text = cleanZikrText(zikrItem.text),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    lineHeight = 32.sp
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = if (isCompleted) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom row with count and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Share button
                IconButton(
                    onClick = { /*shareZikr(context, zikrItem.text)*/ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Count indicator
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                        .clickable(enabled = isCompleted) { onResetClicked() },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.completed),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = remainingCount.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            if (zikrItem.count > 1) {
                                Text(
                                    text = "/ ${zikrItem.count}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                // Reset button (visible when completed)
                if (isCompleted) {
                    IconButton(
                        onClick = onResetClicked,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.reset),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    // Spacer to maintain layout
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }

            // Progress indicator for individual zikr
            if (zikrItem.count > 1) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { completedCount.toFloat() / zikrItem.count },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isCompleted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

private fun cleanZikrText(text: String): String {
    return text.replace("(", "")
        .replace(")", "")
        .replace("]", "")
        .replace("[", "")
        .trim()
}

@SuppressLint("MissingPermission")
private fun vibrate(vibrator: Vibrator) {
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }
}

/*private fun shareZikr(context: Context, zikr: String) {
    val cleanedZikr = cleanZikrText(zikr)
    val intent = Intent(Intent.ACTION_SEND).apply {
        Intent.setType = "text/plain"
        putExtra(Intent.EXTRA_TEXT, cleanedZikr)
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_zikr_subject))
    }
    context.startActivity(
        Intent.createChooser(intent, context.getString(R.string.share_zikr))
    )
}*/

@Preview(name = "ZikrScreen Preview", showBackground = true)
@Composable
private fun ZikrScreenPreview() {
    val fakeAzkarItems = listOf(
        AzkarItemModel(
            audio = "",
            count = 3,
            filename = "",
            id = 1,
            text = "أَصْـبَحْنا وَأَصْـبَحَ المُـلْكُ لله وَالحَمدُ لله ، لا إلهَ إلاّ اللّهُ وَحدَهُ لا شَريكَ لهُ، لهُ المُـلكُ ولهُ الحَمْـد، وهُوَ على كلّ شَيءٍ قدير"
        ),
        AzkarItemModel(
            audio = "",
            count = 1,
            filename = "",
            id = 2,
            text = "الحمد لله رب العالمين"
        )
    )

    val fakeAzkarModel = AzkarModel(
        array = fakeAzkarItems,
        audio = "",
        category = "أذكار الصباح",
        filename = "",
        id = 1
    )

    ZikrScreen(
        zikr = fakeAzkarModel,
        onBackButtonClicked = {}
    )
}
