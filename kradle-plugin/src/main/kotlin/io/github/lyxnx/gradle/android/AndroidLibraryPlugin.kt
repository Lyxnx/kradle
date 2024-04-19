package io.github.lyxnx.gradle.android

import com.android.build.api.dsl.LibraryExtension
import io.github.lyxnx.gradle.internal.ANDROID_LIBRARY_PLUGIN_ID
import io.github.lyxnx.gradle.android.internal.android
import org.gradle.api.Project

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(ANDROID_LIBRARY_PLUGIN_ID)

        configureLibrary(configPlugin.androidOptions)
    }

    private fun Project.configureLibrary(options: AndroidOptions) = android<LibraryExtension> {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }

        buildFeatures {
            buildConfig = false
            resValues = false
            androidResources = false
        }

        lint {
            targetSdk = options.targetSdk.get()
        }

        testOptions {
            targetSdk = options.targetSdk.get()
        }
    }
}
