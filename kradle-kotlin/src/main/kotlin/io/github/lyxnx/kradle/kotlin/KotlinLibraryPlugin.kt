package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.kotlin.dsl.configureJava
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlinTest
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

public class KotlinLibraryPlugin : KradlePlugin() {

    override fun Project.configure() {
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        // If KMP was added by user, don't add the standard version - configure kotlin around their settings
        if (!plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            plugins.apply(KotlinPluginWrapper::class)
        }

        val jvmTarget = kradleExtension.jvmTarget
        configureKotlin(jvmTarget, kradleExtension.kotlinCompilerArgs)
        configureKotlinTest(configPlugin.testOptions)
        configureJava(jvmTarget)
    }
}
