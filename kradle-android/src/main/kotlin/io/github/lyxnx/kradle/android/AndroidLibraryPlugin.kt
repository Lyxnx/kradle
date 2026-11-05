package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import io.github.lyxnx.kradle.android.dsl.android
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.internal.ifExists
import org.gradle.api.Project

public class AndroidLibraryPlugin : SingleTargetAndroidPlugin("com.android.library") {

    override fun configureAndroid(configPlugin: AndroidConfigPlugin) {
        super.configureAndroid(configPlugin)

        project.configureAndroidLibrary(configPlugin.androidExtension)
    }

    private fun Project.configureAndroidLibrary(options: AndroidExtensionImpl) {
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
                    targetSdk {
                        if (options.configureTargetSdk.isPresent) {
                            options.configureTargetSdk.get().invoke(this)
                        } else {
                            version = release(options.targetSdk.get())
                        }
                    }
                }
                it.testOptions {
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
