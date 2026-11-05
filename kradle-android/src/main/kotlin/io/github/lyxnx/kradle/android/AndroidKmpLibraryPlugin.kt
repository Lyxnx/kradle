package io.github.lyxnx.kradle.android

import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.internal.ifExists
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.dsl.getOrElse
import io.github.lyxnx.kradle.dsl.ifPresent
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public class AndroidKmpLibraryPlugin : BaseAndroidPlugin("com.android.kotlin.multiplatform.library") {

    @InternalKradleApi
    override fun configure(project: Project) {
        if (!project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Kotlin Multiplatform Plugin must be applied to use Kradle Android Multiplatform Library plugin")
        }

        super.configure(project)
    }

    override fun configureAndroid(configPlugin: AndroidConfigPlugin) {
        super.configureAndroid(configPlugin)

        project.configureMultiplatformAndroidLibrary(configPlugin.androidExtension)
    }

    @Suppress("UnstableApiUsage")
    private fun Project.configureMultiplatformAndroidLibrary(options: AndroidExtensionImpl) {
        androidComponents<KotlinMultiplatformAndroidComponentsExtension> {
            finalizeDsl { extension ->
                extension.apply {
                    options.buildToolsVersion.ifPresent { buildToolsVersion = it }

                    compileSdk {
                        if (options.configureCompileSdk.isPresent) {
                            options.configureCompileSdk.get().invoke(this)
                        } else {
                            version = release(options.compileSdk.getOrElse {
                                // Fallback to targetSdk as per documentation
                                // Will fail if not present
                                options.targetSdk.get()
                            })
                        }
                    }

                    minSdk = options.minSdk.get()

                    // Don't configure tests for multiple reasons:
                    // 1. Values set here are basically the same as the default anyway
                    // 2. If consumer does the same thing, it will cause an error due to it already being configured
//                    withDeviceTest {
//                        instrumentationRunner = Constants.ANDROIDX_TEST_RUNNER
//                        execution = "HOST"
//                    }
//                    withHostTest {
//                        targetSdk {
//                            version = release(options.targetSdk.get())
//                        }
//                    }
                    lint {
                        targetSdk {
                            version = release(options.targetSdk.get())
                        }
                    }
                    optimization {
                        project.layout.projectDirectory
                            .file("consumer-rules.pro")
                            .ifExists {
                                consumerKeepRules.publish = true
                                consumerKeepRules.files.add(it)
                            }
                    }
                }
            }
        }
    }
}
