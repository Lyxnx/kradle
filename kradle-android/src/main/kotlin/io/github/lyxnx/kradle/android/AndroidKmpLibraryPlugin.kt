package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.api.KotlinMultiplatformAndroidPlugin
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.internal.ifExists
import io.github.lyxnx.kradle.dsl.getOrElse
import io.github.lyxnx.kradle.dsl.ifPresent
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import kotlin.reflect.KClass

public class AndroidKmpLibraryPlugin : BaseAndroidPlugin() {

    override val pluginClass: KClass<out BasePlugin>
        get() = KotlinMultiplatformAndroidPlugin::class

    override fun configure(project: Project) {
        if (!project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Kotlin Multiplatform Plugin must be applied to use Kradle Android Multiplatform Library plugin")
        }

        super.configure(project)
    }

    override fun configureAndroid(options: AndroidOptions) {
        super.configureAndroid(options)

        project.configureMultiplatformAndroidLibrary(options)
    }

    @Suppress("UnstableApiUsage")
    public fun Project.configureMultiplatformAndroidLibrary(options: AndroidOptions) {
        androidComponents<KotlinMultiplatformAndroidComponentsExtension> {
            finalizeDsl { extension ->
                extension.apply {
                    options.buildToolsVersion.ifPresent { buildToolsVersion = it }
                    setCompileSdk(options.compileSdk.getOrElse { options.targetSdk.get().toString() })
                    minSdk = options.minSdk.get()
                    withDeviceTest {
                        instrumentationRunner = Constants.ANDROIDX_TEST_RUNNER
                        execution = "HOST"
                    }
                    withHostTest {
                        targetSdk {
                            version = release(options.targetSdk.get())
                        }
                    }
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

    private fun KotlinMultiplatformAndroidLibraryExtension.setCompileSdk(version: String) {
        val intVersion = version.toIntOrNull()
        if (intVersion != null) {
            compileSdk = intVersion
        } else {
            compileSdkPreview = version
        }
    }

    public fun Project.filterTestTaskDependencies(options: AndroidOptions) {
        afterEvaluate {
            tasks.named("allTests") {
                val testTasksFilter = options.testTasksFilter.get()
                setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
            }
        }
    }
}
