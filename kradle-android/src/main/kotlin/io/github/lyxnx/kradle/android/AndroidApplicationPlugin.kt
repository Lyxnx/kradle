package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import io.github.lyxnx.kradle.android.dsl.android
import io.github.lyxnx.kradle.android.dsl.androidComponents
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun configure(project: Project) {
        if (project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Kotlin Multiplatform plugin is not supported with the Android Application plugin. Only library projects are supported.")
        }

        project.applyBasePlugin(Constants.APPLICATION_PLUGIN_ID)

        project.configureApp()
        project.androidComponents<ApplicationAndroidComponentsExtension> {
            finalizeDsl {
                it.finalizeApp(configPlugin.androidOptions)
            }
        }
    }

    private fun Project.configureApp() = android<ApplicationExtension> {
        defaultConfig {
            proguardFiles(
                "proguard-rules.pro",
                getDefaultProguardFile("proguard-android-optimize.txt")
            )
        }

        buildTypes {
            debug {
                isDebuggable = true
                isMinifyEnabled = false
                isShrinkResources = false
            }

            release {
                isDebuggable = false
                isMinifyEnabled = true
                isShrinkResources = true
            }
        }
    }

    private fun ApplicationExtension.finalizeApp(options: AndroidOptions) {
        defaultConfig {
            targetSdk = options.targetSdk.get()
        }
    }
}
