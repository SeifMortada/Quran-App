import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.seifmortada.applications.quran.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.6.0")
    compileOnly("com.android.tools:common:31.6.0")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
    compileOnly("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.0.20-1.0.24")
    compileOnly("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.0.20")
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "quran.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "quran.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "quran.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "quran.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "quran.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidKoin") {
            id = "quran.android.koin"
            implementationClass = "AndroidKoinConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "quran.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}