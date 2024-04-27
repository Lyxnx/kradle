package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.kotlin.dsl.configureJava
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlinTest
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.findPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

public class KotlinLibraryPlugin : KradlePlugin() {

    override fun Project.configure() {
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        var kotlinPlugin: KotlinBasePluginWrapper? = plugins.findPlugin(KotlinMultiplatformPluginWrapper::class)

        // If KMP was added by user, don't add the standard version - configure kotlin around their settings
        if (kotlinPlugin == null) {
            kotlinPlugin = plugins.apply(KotlinPluginWrapper::class)
        }

        val jvmTarget = kradleExtension.jvmTarget

        configureKotlin(kotlinPlugin, jvmTarget, kradleExtension.kotlinCompilerArgs)
        configureKotlinTest(configPlugin.testOptions)
        configureJava(jvmTarget)
    }
}
