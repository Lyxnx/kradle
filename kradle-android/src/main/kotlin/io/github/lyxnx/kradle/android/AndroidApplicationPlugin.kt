package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import io.github.lyxnx.kradle.android.dsl.BUILD_TYPE_DEBUG
import io.github.lyxnx.kradle.android.internal.android
import io.github.lyxnx.kradle.android.internal.androidComponents
import org.gradle.api.Project

public class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(ANDROID_APPLICATION_PLUGIN_ID)

        configureApp()
        androidComponents<ApplicationAndroidComponentsExtension> {
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
                applicationIdSuffix = ".$BUILD_TYPE_DEBUG"
                isDebuggable = true
                isMinifyEnabled = false
                isShrinkResources = false
            }

            release {
                applicationIdSuffix = ""
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
