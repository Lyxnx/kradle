package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.CommonExtension
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.dsl.test
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.dsl.getOrElse
import io.github.lyxnx.kradle.dsl.ifPresent
import io.github.lyxnx.kradle.kotlin.dsl.setTestOptions
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public abstract class SingleTargetAndroidPlugin(pluginId: String) : BaseAndroidPlugin(pluginId) {

    @InternalKradleApi
    override fun configure(project: Project) {
        if (project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Do not use the Kotlin Multiplatform plugin when adding a single target Kradle plugin")
        }

        super.configure(project)
    }

    override fun configureAndroid(configPlugin: AndroidConfigPlugin) {
        super.configureAndroid(configPlugin)

        project.filterTestTaskDependencies(configPlugin.androidExtension)

        project.androidComponents {
            finalizeDsl { extension ->
                extension.configureBaseAndroidOptions(configPlugin.androidExtension)
            }
        }
    }

    @OptIn(InternalKradleApi::class)
    private fun CommonExtension.configureBaseAndroidOptions(options: AndroidExtensionImpl) {
        options.ndkVersion.ifPresent { ndkVersion = it }
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

        buildFeatures.apply {
            shaders = false
        }

        defaultConfig.apply {
            minSdk = options.minSdk.get()

            testInstrumentationRunner = Constants.ANDROIDX_TEST_RUNNER
        }

        testOptions.apply {
            unitTests.all { it.setTestOptions(options.test) }
        }
    }

    private fun Project.filterTestTaskDependencies(options: AndroidExtension) {
        afterEvaluate {
            val task = tasks.findByName("test") ?: return@afterEvaluate
            val testTasksFilter = options.testTasksFilter.get()
            task.setDependsOn(task.dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
        }
    }
}
