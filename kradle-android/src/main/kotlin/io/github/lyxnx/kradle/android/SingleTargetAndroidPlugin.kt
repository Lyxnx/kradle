package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.CommonExtension
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.dsl.test
import io.github.lyxnx.kradle.dsl.getOrElse
import io.github.lyxnx.kradle.dsl.ifPresent
import io.github.lyxnx.kradle.kotlin.dsl.setTestOptions
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public abstract class SingleTargetAndroidPlugin : BaseAndroidPlugin() {

    override fun configure(project: Project) {
        if (project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Do not use the Kotlin Multiplatform plugin when adding a single target Kradle plugin")
        }

        super.configure(project)
    }

    override fun configureAndroid(options: AndroidOptions) {
        super.configureAndroid(options)

        project.filterTestTaskDependencies(options)

        project.androidComponents {
            finalizeDsl { extension ->
                extension.configureBaseAndroidOptions(options)
            }
        }
    }

    public fun CommonExtension.configureBaseAndroidOptions(options: AndroidOptions) {
        options.ndkVersion.ifPresent { ndkVersion = it }
        options.buildToolsVersion.ifPresent { buildToolsVersion = it }
        setCompileSdk(options.compileSdk.getOrElse { options.targetSdk.get().toString() })

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

    private fun CommonExtension.setCompileSdk(version: String) {
        val intVersion = version.toIntOrNull()
        if (intVersion != null) {
            compileSdk = intVersion
        } else {
            compileSdkPreview = version
        }
    }

    public fun Project.filterTestTaskDependencies(options: AndroidOptions) {
        afterEvaluate {
            tasks.named("test") {
                val testTasksFilter = options.testTasksFilter.get()
                setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
            }
        }
    }
}
