plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.seifmortada.applications.quran.core.ui"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    // Core modules
    api(project(":core:domain"))

    // Compose BOM - critical for version compatibility
    api(platform(libs.androidx.compose.bom))

    // Compose UI bundles (now includes Material3)
    api(libs.bundles.compose.ui)
    api(libs.bundles.compose.integration)

    // Additional Material design
    api(libs.androidx.material)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.testing)
}