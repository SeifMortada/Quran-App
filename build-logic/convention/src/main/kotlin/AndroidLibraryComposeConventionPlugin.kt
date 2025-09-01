import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = QuranBuildConstants.COMPOSE_COMPILER_EXTENSION
                }
            }

            dependencies {
                val bom = libs.findLibrary("androidx.compose.bom").get()
                add("api", platform(bom))
                add("api", libs.findBundle("compose.ui").get())
                add("api", libs.findBundle("compose.integration").get())

                // Material3 (using specific version as in original)
                add("api", "androidx.compose.material3:material3-android:1.5.0-alpha02")
            }
        }
    }
}