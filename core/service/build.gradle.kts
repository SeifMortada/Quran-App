plugins {
    alias(libs.plugins.quran.android.feature)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.media)
    implementation(libs.koin.android)

    api("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}
