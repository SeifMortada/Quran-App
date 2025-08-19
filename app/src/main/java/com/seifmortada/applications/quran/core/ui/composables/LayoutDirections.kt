package com.seifmortada.applications.quran.core.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

// Right to left composable
@Composable
fun ForceRightOrLeft(forceRight:Boolean,modifier: Modifier = Modifier,content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides if (forceRight) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        content()
    }
}