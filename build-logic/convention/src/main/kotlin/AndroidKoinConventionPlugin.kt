import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findBundle("koin").get())
                add("implementation", libs.findLibrary("koin.androidx.compose").get())
            }
        }
    }
}