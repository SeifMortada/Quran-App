plugins {
    alias(libs.plugins.quran.android.library)
    alias(libs.plugins.quran.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seifmortada.applications.quran.core.ui"
}

dependencies {
    implementation(project(":core:domain"))

    api(libs.material)
    api("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}
