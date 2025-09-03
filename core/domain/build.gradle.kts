plugins {
    alias(libs.plugins.quran.android.library)
    alias(libs.plugins.quran.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.seifmortada.applications.quran.core.domain"
}

dependencies {
    api(libs.bundles.koin)
    api(libs.timber)
    api(libs.kotlinx.serialization.json)
}