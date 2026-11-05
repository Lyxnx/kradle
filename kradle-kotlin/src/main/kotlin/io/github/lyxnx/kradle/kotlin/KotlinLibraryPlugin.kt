package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.kotlin.dsl.configureJava
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlin
import io.github.lyxnx.kradle.kotlin.dsl.setTestOptions
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

public class KotlinLibraryPlugin : KradlePlugin() {

    @InternalKradleApi
    override fun configure(project: Project) {
        val configPlugin = project.plugins.apply(KotlinConfigPlugin::class)

        // If KMP was added by user, don't add the standard version - configure kotlin around their settings
        if (!project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            project.plugins.apply(KotlinPluginWrapper::class)
        }

        val jvmTarget = kradleExtension.jvmTarget
        project.configureKotlin(jvmTarget, kradleExtension.kotlinCompilerArgs)
        project.configureKotlinTest(configPlugin.testOptions)
        project.configureJava()
    }
}

@OptIn(InternalKradleApi::class)
private fun Project.configureKotlinTest(options: KotlinTestExtension) {
    tasks.withType<Test>().configureEach {
        setTestOptions(options)
    }
}
