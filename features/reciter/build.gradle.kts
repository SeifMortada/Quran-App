plugins {
    alias(libs.plugins.quran.android.feature)
}

dependencies {
    implementation(project(":core:service"))
    implementation(libs.androidx.media)
}