import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("kotlin-parcelize")
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            tasks.withType(KotlinCompile::class.java).configureEach {
                kotlinOptions {
                    jvmTarget = QuranBuildConstants.JVM_TARGET
                }
            }
        }
    }
}