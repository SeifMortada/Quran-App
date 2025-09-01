plugins {
    alias(libs.plugins.quran.android.library)
}

dependencies {
    // Core modules
    api(project(":core:domain"))
    implementation(project(":core:ui"))

    // Media and Audio
    implementation(libs.bundles.media)

    // Koin
    implementation(libs.bundles.koin)

    // LocalBroadcastManager for download progress broadcasts
    api("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}