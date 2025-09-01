import java.util.Properties

plugins {
    alias(libs.plugins.quran.android.library)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val apiProperties = Properties()
        apiProperties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "BASE_URL", "\"http://api.alquran.cloud/v1/\"")
        buildConfigField("String", "URL_AYAH_RECITATION", "\"https://cdn.islamic.network/quran/audio/64/ar.husary/\"")
        buildConfigField("String", "URL_FETCH_ALL_RECITERS", "\"https://mp3quran.net/api/v3/reciters\"")
    }
}

dependencies {
    api(project(":core:domain"))

    // Room bundle
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Networking bundle
    implementation(libs.bundles.networking)
    implementation(libs.okhttp.urlconnection)
}