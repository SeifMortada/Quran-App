import QuranBuildConstants.JAVA_VERSION
import QuranBuildConstants.JVM_TARGET
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = QuranBuildConstants.COMPILE_SDK

                namespace = "${QuranBuildConstants.APPLICATION_ID}${target.path.replace(":", ".")}"

                defaultConfig {
                    minSdk = QuranBuildConstants.MIN_SDK
                    testInstrumentationRunner = QuranBuildConstants.TEST_RUNNER
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = JAVA_VERSION
                    targetCompatibility = JAVA_VERSION
                }
            }

            extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension> {
                jvmToolchain(JVM_TARGET.toInt())
            }
        }
    }
}