package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import io.github.lyxnx.kradle.android.dsl.android
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.internal.ifExists
import org.gradle.api.Project
import kotlin.reflect.KClass

public class AndroidApplicationPlugin : SingleTargetAndroidPlugin() {

    override val pluginClass: KClass<out BasePlugin>
        get() = AppPlugin::class

    override fun configureAndroid(options: AndroidOptions) {
        super.configureAndroid(options)

        project.configureAndroidApplication(options)
    }

    public fun Project.configureAndroidApplication(options: AndroidOptions) {
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
                    targetSdk = options.targetSdk.get()
                }
            }
        }
    }
}
