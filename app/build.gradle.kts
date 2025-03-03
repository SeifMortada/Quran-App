import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.seifmortada.applications.quran"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seifmortada.applications.quran"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


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
        viewBinding = true
        compose = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":di"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Core Compose Dependencies
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

    // Navigation for Compose
    implementation(libs.navigation.compose)
    implementation (libs.androidx.material)
    implementation(libs.kotlinx.serialization.json)
    // Hilt Navigation for compose
    implementation (libs.koin.androidx.compose)

    // Integration with Activity
    implementation(libs.compose.activity)

    // ViewPager2
    implementation(libs.androidx.viewpager2)

    // Extended icons
    implementation (libs.androidx.material.icons.extended)

    implementation (libs.androidx.media3.exoplayer)
    implementation (libs.androidx.media3.ui)
    implementation (libs.androidx.media3.common)
}
