package com.seifmortada.applications.quran.presentation.common.composables

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ButtonIcon(
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    @ColorRes tintResource: Int,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(iconId),
            contentDescription = null,
            tint = colorResource(tintResource)
        )

    }
}