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

                // Testing
                add("testImplementation", libs.findBundle("testing").get())
                add("androidTestImplementation", libs.findBundle("testing").get())
            }
        }
    }
}