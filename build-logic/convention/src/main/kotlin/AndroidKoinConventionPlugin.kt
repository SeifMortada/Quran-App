import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", "io.insert-koin:koin-core:3.2.2")
                add("implementation", "io.insert-koin:koin-android:3.2.2")
                add("implementation", "io.insert-koin:koin-androidx-compose:3.2.2")
            }
        }
    }
}