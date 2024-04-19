package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.internal.KOTLIN_PLUGIN_ID
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlinTest
import io.github.lyxnx.gradle.kotlin.internal.java
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

public class KotlinLibraryPlugin : KradlePlugin() {

    override fun Project.configure() {
        apply(plugin = KOTLIN_PLUGIN_ID)
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        val jvmTarget = kradleExtension.jvmTarget

        configureKotlin(jvmTarget, kradleExtension.kotlinCompilerArgs)
        configureKotlinTest(configPlugin.testOptions)

        afterEvaluate {
            java {
                targetCompatibility = jvmTarget.get()
                sourceCompatibility = jvmTarget.get()
            }
        }
    }
}
