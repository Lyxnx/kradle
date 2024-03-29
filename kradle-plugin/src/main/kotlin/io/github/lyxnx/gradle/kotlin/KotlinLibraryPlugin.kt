package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlinTest
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

public class KotlinLibraryPlugin : KradlePlugin() {

    override fun Project.configure() {
        apply(plugin = "kotlin")
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        afterEvaluate {
            configureKotlin(configPlugin.jvmTarget)
            configureKotlinTest(configPlugin.testOptions)
        }
    }
}
