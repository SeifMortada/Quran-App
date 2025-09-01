plugins {
    alias(libs.plugins.quran.kotlin.library)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    // Koin bundles
    api(libs.bundles.koin)

    // Timber for logging
    api(libs.timber)

    // Serialization
    api(libs.kotlinx.serialization.json)
}