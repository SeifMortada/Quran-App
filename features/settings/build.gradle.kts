plugins {
    alias(libs.plugins.quran.android.feature)
}

dependencies {
    implementation(project(":core:data"))
    implementation(libs.androidx.datastore)
    implementation("com.android.billingclient:billing-ktx:7.1.1")
}