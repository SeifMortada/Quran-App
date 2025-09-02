plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.seifmortada.applications.quran.core.service"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    // Core modules
    api(project(":core:domain"))
    implementation(project(":core:ui"))

    // Android core
    implementation(libs.androidx.core.ktx)

    // Media and Audio
    implementation(libs.androidx.media)

    // Coroutines
    // implementation(libs.kotlinx.coroutines.android)

    // Koin
    implementation(libs.koin.android)

    // LocalBroadcastManager for download progress broadcasts
    api("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}