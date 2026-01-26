package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.LibraryPlugin
import io.github.lyxnx.kradle.android.dsl.android
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.internal.ifExists
import org.gradle.api.Project
import kotlin.reflect.KClass

public class AndroidLibraryPlugin : SingleTargetAndroidPlugin() {

    override val pluginClass: KClass<out BasePlugin>
        get() = LibraryPlugin::class

    override fun configureAndroid(options: AndroidOptions) {
        super.configureAndroid(options)

        project.configureAndroidLibrary(options)
    }

    public fun Project.configureAndroidLibrary(options: AndroidOptions) {
        android<LibraryExtension> {
            defaultConfig {
                project.layout.projectDirectory
                    .file("consumer-rules.pro")
                    .ifExists { consumerProguardFile(it) }
            }
        }

        androidComponents<LibraryAndroidComponentsExtension> {
            finalizeDsl {
                it.lint {
                    targetSdk = options.targetSdk.get()
                }
                it.testOptions {
                    targetSdk = options.targetSdk.get()
                }
            }
        }
    }
}
