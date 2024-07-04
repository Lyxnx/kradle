package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.kotlin.dsl.configureJavaPlugin
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlinPlugin
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlinTest
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
        configureKotlinPlugin(kotlinPlugin, jvmTarget, kradleExtension.kotlinCompilerArgs)
        configureKotlinTest(configPlugin.testOptions)
        configureJavaPlugin(jvmTarget)
    }
}
