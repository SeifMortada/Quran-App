package com.seifmortada.applications.quran.features.azkar_detail.composables

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AzkarModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.domain.model.AzkarItemModel
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.utils.ButtonIcon
import com.seifmortada.applications.quran.utils.FunctionsUtils
import com.seifmortada.applications.quran.utils.SearchToolbar
import com.seifmortada.applications.quran.utils.SearchTopAppBar


@Composable
fun ZikrScreen(
    zikrId:Int,
    azkars: AzkarModel= AzkarModel(
        array = emptyList(),
        audio = "",
        category = "",
        filename = "",
        id = 0
    ),
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    var searchQuery by remember { mutableStateOf("") }
    var filterdAzkars = azkars.array.filter {
        FunctionsUtils.normalizeTextForFiltering(it.text)
            .contains(searchQuery, ignoreCase = true)
    }
    var isSearch by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isSearch) {
                SearchToolbar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { newQuery -> searchQuery = newQuery },
                    onSearchTriggered = { isSearch=false },
                    onBackClick = onBackButtonClicked
                )
            } else {
                SearchTopAppBar(
                    title = azkars.category,
                    onBackClick = onBackButtonClicked,
                    onSearchClick = { isSearch = it }
                )
            }
        }) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(filterdAzkars) { zikr ->
                AzkarCard(
                    zikr
                )
            }
        }
    }

}

@Composable
private fun AzkarCard(
    zikr: AzkarItemModel, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    var zikrCount by remember { mutableStateOf(zikr.count) }
    Card(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clickable(
                enabled = true,
                onClick = {
                    if (zikrCount > 0) {
                        vibrate(vibrator = vibrator)
                        zikrCount--
                    }
                }
            )

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val filteredZikr =
                zikr.text.replace("(", "").replace(")", "").replace("]", "").replace("[", "")
            Text(
                text = filteredZikr,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.padding(start = 10.dp),
                )
                {
                    if (zikrCount > 0) {
                        Icon(
                            modifier = Modifier
                                .size(40.dp), painter = painterResource(R.drawable.ic_ayah),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )

                        Text(
                            text = zikrCount.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    } else {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                tint = colorResource(R.color.md_theme_primary),
                                modifier = Modifier.size(25.dp),
                                contentDescription = null
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Absolute.Right,
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom
                ) {

                    ButtonIcon(
                        onClick = {},
                        iconId = R.drawable.ic_bookmark,
                        tintResource = R.color.md_theme_primary
                    )
                    ButtonIcon(onClick = {
                        shareZikr(context = context, zikr = zikr.text)
                    }, iconId = R.drawable.ic_share, tintResource = R.color.md_theme_primary)
                }
            }
        }

    }
}


private fun vibrate(vibrator: Vibrator) {
    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, 100))
        } else {
            vibrator.vibrate(50)
        }
    }
}

private fun shareZikr(context: Context, zikr: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, zikr)
    }
    context.startActivity(
        Intent.createChooser(intent, "Share Zikr")
    )
}

@Preview(name = "AzkarScreen Preview", showBackground = false)
@Composable
private fun AzkarScreenPreview() {
    val fakeAzkarItems = listOf(
        AzkarItemModel(
            audio = "https://example.com/audio1.mp3",
            count = 3,
            filename = "azkar_morning.mp3",
            id = 1,
            text = "أَصْـبَحْنا وَأَصْـبَحَ المُـلْكُ لله وَالحَمدُ لله ، لا إلهَ إلاّ اللّهُ وَحدَهُ لا شَريكَ لهُ، لهُ المُـلكُ ولهُ الحَمْـد، وهُوَ على كلّ شَيءٍ قدير ، رَبِّ أسْـأَلُـكَ خَـيرَ ما في هـذا اليوم وَخَـيرَ ما بَعْـدَه ، وَأَعـوذُ بِكَ مِنْ شَـرِّ ما في هـذا اليوم وَشَرِّ ما بَعْـدَه، رَبِّ أَعـوذُبِكَ مِنَ الْكَسَـلِ وَسـوءِ الْكِـبَر ، رَبِّ أَعـوذُ بِكَ مِنْ عَـذابٍ في النّـارِ وَعَـذابٍ في القَـبْر."
        ),
        AzkarItemModel(
            audio = "https://example.com/audio2.mp3",
            count = 5,
            filename = "azkar_evening.mp3",
            id = 2,
            text = "الحمد لله"
        ),
        AzkarItemModel(
            audio = "https://example.com/audio3.mp3",
            count = 1,
            filename = "azkar_night.mp3",
            id = 3,
            text = "الله أكبر"
        )
    )

    val fakeAzkarModel = AzkarModel(
        array = fakeAzkarItems,
        audio = "https://example.com/main_audio.mp3",
        category = "Morning Azkar",
        filename = "azkar_collection.mp3",
        id = 101
    )


}