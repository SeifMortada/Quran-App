import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("com.google.devtools.ksp")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = QuranBuildConstants.COMPILE_SDK

                defaultConfig {
                    applicationId = QuranBuildConstants.APPLICATION_ID
                    minSdk = QuranBuildConstants.MIN_SDK
                    targetSdk = QuranBuildConstants.TARGET_SDK
                    versionCode = QuranBuildConstants.VERSION_CODE
                    versionName = QuranBuildConstants.VERSION_NAME

                    testInstrumentationRunner = QuranBuildConstants.TEST_RUNNER
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
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildFeatures {
                    viewBinding = true
                }
            }
        }
    }
}