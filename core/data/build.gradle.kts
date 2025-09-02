import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.seifmortada.applications.quran.core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

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

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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