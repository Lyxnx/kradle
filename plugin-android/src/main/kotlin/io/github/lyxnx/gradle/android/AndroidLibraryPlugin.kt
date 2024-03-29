package io.github.lyxnx.gradle.android

import com.android.build.api.dsl.LibraryExtension
import io.github.lyxnx.gradle.android.internal.android
import org.gradle.api.Project

private const val ANDROID_LIBRARY_PLUGIN = "com.android.library"

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(ANDROID_LIBRARY_PLUGIN)

        android<LibraryExtension> {
            defaultConfig {
                consumerProguardFiles("consumer-rules.pro")
            }

            buildFeatures {
                buildConfig = false
                resValues = false
                androidResources = false
            }

            lint {
                targetSdk = configPlugin.androidOptions.targetSdk.get()
            }

            val compileVersion = configPlugin.androidOptions.compileSdk.get()
            val intVersion = compileVersion.toIntOrNull()
            testOptions {
                if (intVersion != null) {
                    targetSdk = intVersion
                } else {
                    targetSdkPreview = compileVersion
                }
            }
        }
    }
}
