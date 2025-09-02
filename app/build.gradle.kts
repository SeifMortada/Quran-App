plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seifmortada.applications.quran"
    compileSdk = 35

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
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose BOM - ensures all Compose libraries use compatible versions
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI bundles (includes Material3)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.integration)

    // Explicit Material3 and Google Material Design dependencies
    implementation(libs.androidx.material3)
    implementation(libs.material)

    // AppCompat for compatibility
    implementation(libs.androidx.appcompat)

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

    // App-specific libraries (not covered by conventions)
    implementation(libs.androidx.media)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)

    // External libraries not in TOML
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("com.android.billingclient:billing-ktx:7.1.1")
}