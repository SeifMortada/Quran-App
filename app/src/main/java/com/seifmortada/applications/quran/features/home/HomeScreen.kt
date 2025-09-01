package com.seifmortada.applications.quran.features.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import com.seifmortada.applications.quran.core.domain.model.main.MainItem
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
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =
                            if (layoutDirection == LayoutDirection.Ltr)
                                Arrangement.Start
                            else
                                Arrangement.End
                    ) {
                        if (layoutDirection == LayoutDirection.Ltr) {
                            Image(
                                painter = painterResource(id = R.drawable.quran_app_logo),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Column(
                            horizontalAlignment =
                                if (layoutDirection == LayoutDirection.Ltr)
                                    Alignment.Start
                                else
                                    Alignment.End
                        ) {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = if (layoutDirection == LayoutDirection.Ltr) TextAlign.Start else TextAlign.End
                            )
                            Text(
                                text = stringResource(R.string.app_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = if (layoutDirection == LayoutDirection.Ltr) TextAlign.Start else TextAlign.End
                            )
                        }
                        if (layoutDirection == LayoutDirection.Rtl) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Image(
                                painter = painterResource(id = R.drawable.quran_app_logo),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                WelcomeSection()
            }

            item {
                MainFeaturesSection(
                    mainItems = mainItems,
                    onZikrClick = onZikrClick,
                    onQuranClick = onQuranClick,
                    onReciterClick = onReciterClick
                )
            }

            item {
                QuickAccessSection(
                    onQuranClick = onQuranClick,
                    onReciterClick = onReciterClick
                )
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.welcome_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
private fun MainFeaturesSection(
    mainItems: List<MainItem>,
    onZikrClick: () -> Unit,
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    Column {
        Text(
            text = stringResource(R.string.main_features),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = if (layoutDirection == LayoutDirection.Ltr) TextAlign.Start else TextAlign.End
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            mainItems.forEach { item ->
                EnhancedMainItemCard(
                    item = item,
                    onZikrClick = onZikrClick,
                    onQuranClick = onQuranClick,
                    onReciterClick = onReciterClick
                )
            }
        }
    }
}

@Composable
private fun EnhancedMainItemCard(
    item: MainItem,
    onZikrClick: () -> Unit,
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    val zikrsTitle = stringResource(R.string.zikrs)
    val quranTitle = stringResource(R.string.quran)
    val recitersTitle = stringResource(R.string.quran_readers)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when (item.title) {
                    zikrsTitle -> onZikrClick()
                    quranTitle -> onQuranClick()
                    recitersTitle -> onReciterClick()
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (layoutDirection == LayoutDirection.Ltr)
                Arrangement.Start
            else
                Arrangement.End
        ) {
            if (layoutDirection == LayoutDirection.Rtl) {
                Icon(
                    imageVector = when (item.title) {
                        quranTitle -> Icons.Default.Book
                        recitersTitle -> Icons.Default.RecordVoiceOver
                        else -> Icons.Default.LibraryBooks
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))
            }

            if (layoutDirection == LayoutDirection.Ltr) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = item.title,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment =
                    if (layoutDirection == LayoutDirection.Ltr)
                        Alignment.Start
                    else
                        Alignment.End
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = if (layoutDirection == LayoutDirection.Ltr) TextAlign.Start else TextAlign.End
                )

                Text(
                    text = when (item.title) {
                        quranTitle -> stringResource(R.string.quran_description)
                        recitersTitle -> stringResource(R.string.reciters_description)
                        zikrsTitle -> stringResource(R.string.zikrs_description)
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp,
                    textAlign = if (layoutDirection == LayoutDirection.Ltr) TextAlign.Start else TextAlign.End
                )
            }

            if (layoutDirection == LayoutDirection.Ltr) {
                Icon(
                    imageVector = when (item.title) {
                        quranTitle -> Icons.Default.Book
                        recitersTitle -> Icons.Default.RecordVoiceOver
                        else -> Icons.Default.LibraryBooks
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = item.title,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickAccessSection(
    onQuranClick: () -> Unit,
    onReciterClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    Column {
        Text(
            text = stringResource(R.string.quick_access),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = if (layoutDirection == LayoutDirection.Ltr) TextAlign.Start else TextAlign.End
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = layoutDirection == LayoutDirection.Rtl
        ) {
            item {
                QuickAccessCard(
                    title = stringResource(R.string.read_quran),
                    icon = Icons.Default.Book,
                    onClick = onQuranClick
                )
            }

            item {
                QuickAccessCard(
                    title = stringResource(R.string.listen_recitation),
                    icon = Icons.Default.RecordVoiceOver,
                    onClick = onReciterClick
                )
            }
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    QuranAppTheme {
        HomeScreen(
            mainItems = listOf(
                MainItem("الأذكار", R.drawable.ic_tasbih),
                MainItem("القرآن الكريم", R.drawable.ic_koran),
                MainItem("قراء القرآن", R.drawable.ic_imam)
            ), {}, {}, {}
        )
    }
}
