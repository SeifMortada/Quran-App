import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.kotlinOptions
import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.quran.android.feature)
}

dependencies {
    // Feature-specific dependencies (if any) would go here
    // All core modules, Compose, navigation, testing are auto-included by the convention
}