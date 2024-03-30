@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.android.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.internal.android
import io.github.lyxnx.gradle.android.internal.androidComponents
import io.github.lyxnx.gradle.android.internal.test
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.setTestOptions
import io.github.lyxnx.gradle.kotlin.internal.getJavaVersion
import org.gradle.api.GradleException
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
        if (project == rootProject) {
            throw GradleException("Kradle plugin requesting '$pluginId' cannot be applied to the root project")
        }

        val configPlugin = plugins.apply(AndroidConfigPlugin::class)

        apply {
            plugin(pluginId)
            plugin("kotlin-android")
        }

        if (configPlugin.hasCacheFixPlugin) {
            apply(plugin = CACHE_FIX_PLUGIN)
        }

        configureKotlin(configPlugin.jvmTarget)

        dependencies {
            add("androidTestImplementation", kotlin("test"))
        }

        android<AndroidCommonExtension> {
            buildFeatures {
                aidl = false
                shaders = false
                renderScript = false
            }
        }

        androidComponents {
            finalizeDsl { extension ->
                configPlugin.run {
                    extension.apply {
                        val compileSdkVer = androidOptions.compileSdk.get()
                        val intVersion = compileSdkVer.toIntOrNull()
                        if (intVersion != null) {
                            compileSdk = intVersion
                        } else {
                            compileSdkPreview = compileSdkVer
                        }

                        defaultConfig {
                            minSdk = androidOptions.minSdk.get()

                            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                        }

                        compileOptions {
                            getJavaVersion(configPlugin.jvmTarget).let {
                                sourceCompatibility = it
                                targetCompatibility = it
                            }
                        }

                        testOptions {
                            unitTests.all { it.setTestOptions(androidOptions.test) }
                        }
                    }
                }
            }
        }

        afterEvaluate {
            tasks.named("test") {
                val testTasksFilter = configPlugin.androidOptions.testTasksFilter.get()
                setDependsOn(dependsOn.filter { it !is TaskProvider<*> || testTasksFilter(it) })
            }
        }
    }
}
