package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.gradle.kotlin.internal.java
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

public class KotlinLibraryPlugin : KradlePlugin() {

    override fun Project.configure() {
        apply(plugin = "kotlin")
        val configPlugin = plugins.apply(KotlinConfigPlugin::class)

        configureKotlin(configPlugin.jvmTarget)
        configureKotlinTest(configPlugin.testOptions)

        afterEvaluate {
            java {
                targetCompatibility = configPlugin.jvmTarget.get()
                sourceCompatibility = configPlugin.jvmTarget.get()
            }
        }
    }

    private fun Project.configureKotlinTest(options: KotlinTestOptions) {
        tasks.withType<Test>().configureEach { setTestOptions(options) }
    }
}

public fun Test.setTestOptions(options: KotlinTestOptions) {
    val configure = (options as KotlinTestOptionsImpl).configuration.get()
    if (options.useJunitPlatform.get()) {
        useJUnitPlatform(configure)
        testLogging { events("passed", "skipped", "failed") }
    } else {
        useJUnit(configure)
    }
}
