plugins {
    alias(libs.plugins.quran.android.application)
    alias(libs.plugins.quran.android.application.compose)
    alias(libs.plugins.google.devtools.ksp)
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
