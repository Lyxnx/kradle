@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.internal.ANDROIDX_TEST_RUNNER
import io.github.lyxnx.gradle.internal.ANDROID_CACHE_FIX_PLUGIN_ID
import io.github.lyxnx.gradle.internal.KOTLIN_ANDROID_PLUGIN_ID
import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.android.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.internal.android
import io.github.lyxnx.gradle.android.internal.androidComponents
import io.github.lyxnx.gradle.android.internal.test
import io.github.lyxnx.gradle.dsl.ifPresent
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.setTestOptions
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.kotlin

public abstract class BaseAndroidPlugin internal constructor() : KradlePlugin() {

    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    protected fun Project.applyBasePlugin(pluginId: String) {
        if (project == rootProject) {
            throw GradleException("Kradle plugin requesting '$pluginId' cannot be applied to the root project")
        }

        val configPlugin = plugins.apply(AndroidConfigPlugin::class)

        apply {
            plugin(pluginId)
            plugin(KOTLIN_ANDROID_PLUGIN_ID)

            if (configPlugin.hasCacheFixPlugin) {
                plugin(ANDROID_CACHE_FIX_PLUGIN_ID)
            }
        }

        configureKotlin(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)
        configureAndroid()

        dependencies {
            add("androidTestImplementation", kotlin("test"))
        }

        androidComponents {
            finalizeDsl { extension ->
                configPlugin.run {
                    extension.applyAndroidOptions(
                        options = androidOptions,
                        jvmTarget = kradleExtension.jvmTarget
                    )
                    filterTestTaskDependencies(androidOptions)
                }
            }
        }
    }

    private fun Project.configureAndroid() = android<AndroidCommonExtension> {
        buildFeatures {
            aidl = false
            shaders = false
            renderScript = false
        }
    }

    private fun AndroidCommonExtension.applyAndroidOptions(
        options: AndroidOptions,
        jvmTarget: Provider<JavaVersion>
    ) {
        options.ndkVersion.ifPresent { ndkVersion = it }
        options.buildToolsVersion.ifPresent { buildToolsVersion = it }
        setCompileSdk(options.compileSdk.get())

        defaultConfig {
            minSdk = options.minSdk.get()

            testInstrumentationRunner = ANDROIDX_TEST_RUNNER
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

    private fun Project.filterTestTaskDependencies(options: AndroidOptions) {
        afterEvaluate {
            tasks.named("test") {
                val testTasksFilter = options.testTasksFilter.get()
                setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
            }
        }
    }
}
