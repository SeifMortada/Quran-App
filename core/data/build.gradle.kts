plugins {
    alias(libs.plugins.quran.android.library)
    alias(libs.plugins.quran.android.library.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seifmortada.applications.quran.core.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"http://api.alquran.cloud/v1/\"")
        buildConfigField(
            "String",
            "URL_AYAH_RECITATION",
            "\"https://cdn.islamic.network/quran/audio/64/ar.husary/\""
        )
        buildConfigField(
            "String",
            "URL_FETCH_ALL_RECITERS",
            "\"https://mp3quran.net/api/v3/reciters\""
        )
    }
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Networking using TOML catalog
    implementation(libs.bundles.networking)

    // Testing dependencies already provided by convention plugin
}