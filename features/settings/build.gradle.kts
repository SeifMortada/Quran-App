plugins {
    alias(libs.plugins.quran.android.feature)
}

dependencies {
    // Additional module dependency for this feature
    implementation(project(":core:data"))

    // DataStore for settings persistence  
    implementation(libs.androidx.datastore)

    // Google Play Billing for in-app purchases (if needed for donations)
    implementation("com.android.billingclient:billing-ktx:7.1.1")
}