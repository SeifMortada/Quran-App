import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("quran.android.library")
                apply("quran.android.library.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                // Core modules - common to all features
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:di"))
                add("implementation", project(":core:ui"))

                // Basic testing dependencies
                add("testImplementation", "junit:junit:4.13.2")
                add("androidTestImplementation", "androidx.test.ext:junit:1.2.1")
                add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.6.1")
            }
        }
    }
}