import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = QuranBuildConstants.COMPOSE_COMPILER_EXTENSION
                }
            }

            dependencies {
                add("implementation", platform("androidx.compose:compose-bom:2024.08.00"))

                add("implementation", "androidx.compose.ui:ui")
                add("implementation", "androidx.compose.ui:ui-graphics")
                add("implementation", "androidx.compose.ui:ui-tooling-preview")
                add("implementation", "androidx.compose.runtime:runtime")
                add("implementation", "androidx.compose.material:material-icons-extended")

                add("implementation", "androidx.navigation:navigation-compose:2.8.8")
                add("implementation", "io.insert-koin:koin-androidx-compose:3.2.2")
                add("implementation", "androidx.activity:activity-compose:1.9.1")
                add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

                add("implementation", "androidx.compose.material3:material3")
                add("implementation", "androidx.compose.ui:ui-tooling")
            }
        }
    }
}