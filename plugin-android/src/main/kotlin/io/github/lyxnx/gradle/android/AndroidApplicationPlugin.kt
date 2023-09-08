package io.github.lyxnx.gradle.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import io.github.lyxnx.gradle.android.dsl.BUILD_TYPE_DEBUG
import io.github.lyxnx.gradle.android.internal.android
import io.github.lyxnx.gradle.android.internal.androidComponents
import org.gradle.api.Project

private const val ANDROID_APPLICATION_PLUGIN = "com.android.application"

public class AndroidApplicationPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(ANDROID_APPLICATION_PLUGIN)

        configureApp()
        androidComponents<ApplicationAndroidComponentsExtension> {
            finalizeDsl {
                it.apply {
                    defaultConfig {
                        targetSdk = configPlugin.androidOptions.targetSdk.get()
                    }
                }
            }
        }
    }

    private fun Project.configureApp() = android<ApplicationExtension> {
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
}
