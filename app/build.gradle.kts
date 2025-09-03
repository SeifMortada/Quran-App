plugins {
    alias(libs.plugins.quran.android.application)
    alias(libs.plugins.quran.android.application.compose)
}

android {
    namespace = "com.seifmortada.applications.quran"
}

dependencies {
    // Core modules
    implementation(project(":core:domain"))
    implementation(project(":core:di"))
    implementation(project(":core:ui"))
    implementation(project(":core:service"))

    // Feature modules
    implementation(project(":features:quran"))
    implementation(project(":features:zikr"))
    implementation(project(":features:reciter"))
    implementation(project(":features:settings"))

    // App-specific libraries
    implementation(libs.bundles.media)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.material)

    // External libraries
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("com.android.billingclient:billing-ktx:7.1.1")
}