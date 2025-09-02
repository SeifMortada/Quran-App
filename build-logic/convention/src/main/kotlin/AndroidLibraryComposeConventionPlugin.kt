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
                add("api", platform("androidx.compose:compose-bom:2024.08.00"))

                // Compose UI core
                add("api", "androidx.compose.ui:ui")
                add("api", "androidx.compose.ui:ui-graphics")
                add("api", "androidx.compose.ui:ui-tooling-preview")
                add("api", "androidx.compose.runtime:runtime")
                add("api", "androidx.compose.material:material-icons-extended")

                // Navigation and integration
                add("api", "androidx.navigation:navigation-compose:2.8.8")
                add("api", "io.insert-koin:koin-androidx-compose:3.2.2")
                add("api", "androidx.activity:activity-compose:1.9.1")
                add("api", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

                // Material3
                add("api", "androidx.compose.material3:material3")
            }
        }
    }
}