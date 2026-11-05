package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import io.github.lyxnx.kradle.android.dsl.android
import io.github.lyxnx.kradle.android.dsl.androidComponents
import org.gradle.api.Project

public class AndroidApplicationPlugin : SingleTargetAndroidPlugin("com.android.application") {

    override fun configureAndroid(configPlugin: AndroidConfigPlugin) {
        super.configureAndroid(configPlugin)

        project.configureAndroidApplication(configPlugin.androidExtension)
    }

    private fun Project.configureAndroidApplication(options: AndroidExtensionImpl) {
        android<ApplicationExtension> { // These don't read from anything, so safe to configure these here
            buildTypes {
                release {
                    isDebuggable = false
                    isMinifyEnabled = true
                    isShrinkResources = true

                    // https://developer.android.com/topic/performance/app-optimization/keep-rules-overview#optimization-dsl
                    // AGP 9.3+ includes the proguard-android-optimize file by default as well as those in src/<variant>/keepRules
                    optimization {
                        enable = true
                    }
                }
            }
        }

        androidComponents<ApplicationAndroidComponentsExtension> {
            finalizeDsl {
                it.defaultConfig {
                    targetSdk {
                        if (options.configureTargetSdk.isPresent) {
                            options.configureTargetSdk.get().invoke(this)
                        } else {
                            version = release(options.targetSdk.get())
                        }
                    }
                }
            }
        }
    }
}
