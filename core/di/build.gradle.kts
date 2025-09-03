plugins {
    alias(libs.plugins.quran.android.library)
    alias(libs.plugins.quran.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seifmortada.applications.quran.core.di"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
}