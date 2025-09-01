import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.kotlinOptions
import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.quran.android.library)
    alias(libs.plugins.quran.android.library.compose)
}

dependencies {
    // Core modules
    api(project(":core:domain"))

}