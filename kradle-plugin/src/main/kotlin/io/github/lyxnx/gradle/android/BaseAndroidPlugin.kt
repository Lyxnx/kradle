@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.android.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.internal.androidComponents
import io.github.lyxnx.gradle.android.internal.test
import io.github.lyxnx.gradle.dsl.ifPresent
import io.github.lyxnx.gradle.internal.ANDROIDX_TEST_RUNNER
import io.github.lyxnx.gradle.internal.ANDROID_CACHE_FIX_PLUGIN_ID
import io.github.lyxnx.gradle.internal.KOTLIN_ANDROID_PLUGIN_ID
import io.github.lyxnx.gradle.kotlin.dsl.applyKotlinOptions
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.setTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.findPlugin
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

public abstract class BaseAndroidPlugin internal constructor() : KradlePlugin() {

    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    protected lateinit var kotlinPlugin: KotlinBasePluginWrapper
        private set

    protected fun Project.applyBasePlugin(pluginId: String) {
        val configPlugin = plugins.apply(AndroidConfigPlugin::class)

        apply(plugin = pluginId)

        val multiplatformPlugin = plugins.findPlugin(KotlinMultiplatformPluginWrapper::class)

        if (multiplatformPlugin == null) {
            apply(plugin = KOTLIN_ANDROID_PLUGIN_ID)
            kotlinPlugin = plugins.getPlugin(KotlinAndroidPluginWrapper::class)
        } else {
            kotlinPlugin = multiplatformPlugin
        }

        if (configPlugin.hasCacheFixPlugin) {
            apply(plugin = ANDROID_CACHE_FIX_PLUGIN_ID)
        }

        configureKotlin(kotlinPlugin, kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)

        if (kotlinPlugin is KotlinMultiplatformPluginWrapper) {
            configureKMPTarget(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)
        }

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

    private fun AndroidCommonExtension.configureBaseAndroidOptions(
        options: AndroidOptions,
        jvmTarget: Provider<JavaVersion>
    ) {
        options.ndkVersion.ifPresent { ndkVersion = it }
        options.buildToolsVersion.ifPresent { buildToolsVersion = it }
        setCompileSdk(options.compileSdk.get())

        buildFeatures {
            aidl = false
            shaders = false
            renderScript = false
        }

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

    private fun Project.configureKMPTarget(jvmTarget: Provider<JavaVersion>, compilerArgs: Provider<Set<String>>) {
        extensions.getByType<KotlinMultiplatformExtension>().apply {
            targets.withType<KotlinAndroidTarget> {
                compilations.configureEach {
                    kotlinOptions.applyKotlinOptions(jvmTarget, compilerArgs)
                }
            }
        }
    }
}
