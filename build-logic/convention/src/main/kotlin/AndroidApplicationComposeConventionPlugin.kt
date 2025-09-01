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
                val bom = libs.findLibrary("androidx.compose.bom").get()
                add("implementation", platform(bom))
                add("implementation", libs.findBundle("compose.ui").get())
                add("implementation", libs.findBundle("compose.integration").get())

                // Material3 (using specific version as in original)
                add("implementation", "androidx.compose.material3:material3-android:1.5.0-alpha02")

                add("implementation", libs.findLibrary("androidx.material").get())
                add("implementation", libs.findLibrary("androidx.ui.tooling").get())
            }
        }
    }
}