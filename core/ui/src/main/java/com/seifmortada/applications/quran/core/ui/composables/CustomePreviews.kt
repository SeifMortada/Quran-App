package com.seifmortada.applications.quran.core.ui.composables

import androidx.compose.ui.tooling.preview.Preview

// Light theme preview
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO
)
annotation class LightPreview

// Dark theme preview
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
annotation class DarkPreview

// Arabic preview
@Preview(
    name = "Arabic",
    showBackground = true,
    locale = "ar"
)
annotation class ArabicPreview

// English preview
@Preview(
    name = "English",
    showBackground = true,
    locale = "en"
)
annotation class EnglishPreview

@LightPreview
@DarkPreview
annotation class ThemePreviews

@ArabicPreview
@EnglishPreview
annotation class LanguagePreviews
