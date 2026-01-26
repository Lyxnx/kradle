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
            defaultConfig {
                proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
                project.layout.projectDirectory
                    .file("proguard-rules.pro")
                    .ifExists { proguardFile(it) }
            }

            buildTypes {
                release {
                    isDebuggable = false
                    isMinifyEnabled = true
                    isShrinkResources = true
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
