package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.LibraryExtension
import io.github.lyxnx.kradle.android.dsl.android
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun configure(project: Project) {
        if (project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Kradle Android Library plugin no longer supports Kotlin Multiplatform. Use the Kradle Android Multiplatform Library plugin instead")
        }

        project.applyBasePlugin(Constants.LIBRARY_PLUGIN_ID)

        project.configureAndroidLibrary(configPlugin.androidOptions)
    }
}

public fun Project.configureAndroidLibrary(options: AndroidOptions) {
    android<LibraryExtension> {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }

        buildFeatures {
            buildConfig = false
            resValues = false
        }

        androidResources.enable = false

        // For some reason these are being set instantly and not being delayed
        // In multi module projects this is fine but for single module projects this is not and trying to set the
        // targetSdk will cause an error to be thrown that the value cannot be set after being read
        afterEvaluate {
            lint {
                targetSdk = options.targetSdk.get()
            }

            testOptions {
                targetSdk = options.targetSdk.get()
            }
        }
    }
}
