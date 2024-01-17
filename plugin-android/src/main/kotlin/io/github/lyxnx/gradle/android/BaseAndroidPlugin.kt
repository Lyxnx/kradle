@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.android.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.internal.android
import io.github.lyxnx.gradle.android.internal.androidComponents
import io.github.lyxnx.gradle.android.internal.test
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.setTestOptions
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.kotlin

public abstract class BaseAndroidPlugin internal constructor() : KradlePlugin() {

    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    protected fun Project.applyBasePlugin(pluginId: String) {
        val configPlugin = plugins.apply(AndroidConfigPlugin::class)
        apply {
            plugin(pluginId)
            plugin("kotlin-android")
        }

        if (configPlugin.hasCacheFixPlugin) {
            apply(plugin = CACHE_FIX_PLUGIN)
        } else {
            logger.warn(
                """
                Could not find Android cache fix plugin. It is recommended to apply this plugin, but not required.
                (See https://github.com/gradle/android-cache-fix-gradle-plugin)
                
                If it would like to be used, make sure it is added as a build dependency
                E.g. Add using apply false to the root project:
                plugins {
                    id("$CACHE_FIX_PLUGIN") version "<version>" apply false
                }
                """.trimIndent()
            )
        }

        // configureKotlin uses a jvm toolchain, which AGP 8.1.0-alpha09+ will grab the JDK version from and setup the
        // sourceCompatibility + targetCompatibility values to match
        configureKotlin(configPlugin.jvmTarget)
        configureKotlinAndroid()
        configureAndroid()
        androidComponents {
            finalizeDsl { extension ->
                configPlugin.run {
                    extension.applyAndroidOptions(androidOptions)
                    filterTestTaskDependencies(androidOptions)
                }
            }
        }
    }

    private fun Project.configureKotlinAndroid() {
        dependencies {
            add("androidTestImplementation", kotlin("test"))
        }
    }

    private fun Project.configureAndroid() = android<AndroidCommonExtension> {
        buildFeatures {
            aidl = false
            shaders = false
            renderScript = false
        }
    }

    private fun AndroidCommonExtension.applyAndroidOptions(options: AndroidOptions) {
        setCompileSdkVersion(options.compileSdk.get())

        defaultConfig {
            minSdk = options.minSdk.get()
        }

        testOptions {
            unitTests.all { it.setTestOptions(options.test) }
        }
    }

    private fun AndroidCommonExtension.setCompileSdkVersion(version: String) {
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
