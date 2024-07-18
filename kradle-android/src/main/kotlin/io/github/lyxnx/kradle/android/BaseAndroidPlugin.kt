@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.kradle.android

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.android.dsl.AndroidCommonExtension
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.android.dsl.test
import io.github.lyxnx.kradle.dsl.getOrElse
import io.github.lyxnx.kradle.dsl.ifPresent
import io.github.lyxnx.kradle.dsl.isRoot
import io.github.lyxnx.kradle.kotlin.dsl.applyKotlinOptions
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.kradle.kotlin.dsl.setTestOptions
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

public abstract class BaseAndroidPlugin internal constructor() : KradlePlugin() {

    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    protected fun Project.applyBasePlugin(pluginId: String) {
        // Only allow it to be applied to subprojects but if it's a single module project (no submodules) then allow it
        if (isRoot && rootProject.subprojects.isNotEmpty()) {
            throw GradleException("Android plugins can only be applied to subprojects, not the root project")
        }

        val configPlugin = plugins.apply(AndroidConfigPlugin::class)

        apply(plugin = pluginId)

        if (!plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            apply(plugin = Constants.KOTLIN_ANDROID_PLUGIN_ID)
        }

        if (configPlugin.hasCacheFixPlugin) {
            apply(plugin = Constants.CACHEFIX_PLUGIN_ID)
        }

        configureKotlin(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)
        configureKMPTarget(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)

        androidComponents {
            finalizeDsl { extension ->
                configPlugin.run {
                    extension.configureBaseAndroidOptions(
                        options = androidOptions,
                        jvmTarget = kradleExtension.jvmTarget
                    )
                    filterTestTaskDependencies(androidOptions)
                }
            }
        }
    }
}

public fun AndroidCommonExtension.configureBaseAndroidOptions(
    options: AndroidOptions,
    jvmTarget: Provider<JavaVersion>
) {
    options.ndkVersion.ifPresent { ndkVersion = it }
    options.buildToolsVersion.ifPresent { buildToolsVersion = it }
    setCompileSdk(options.compileSdk.getOrElse { options.targetSdk.get().toString() })
    buildFeatures {
        aidl = false
        shaders = false
        renderScript = false
    }

    defaultConfig {
        minSdk = options.minSdk.get()

        testInstrumentationRunner = Constants.ANDROIDX_TEST_RUNNER
    }

    compileOptions {
        // This configures the standard java {} source/target compatibilities so don't need to do it here
        sourceCompatibility = jvmTarget.get()
        targetCompatibility = jvmTarget.get()
    }

    testOptions {
        unitTests.all { it.setTestOptions(options.test) }
    }
}

private fun AndroidCommonExtension.setCompileSdk(version: String) {
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

public fun Project.configureKMPTarget(jvmTarget: Provider<JavaVersion>, compilerArgs: Provider<Set<String>>) {
    extensions.findByType<KotlinMultiplatformExtension>()?.apply {
        targets.withType<KotlinAndroidTarget> {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions.applyKotlinOptions(jvmTarget, compilerArgs)
                }
            }
        }
    }
}
