plugins {
    alias(libs.plugins.quran.android.feature)
}

dependencies {
    // Additional module dependency for this feature
    implementation(project(":core:service"))

    // Media for audio playback
    implementation(libs.androidx.media)
}