@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.kradle.android

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.dsl.isRoot
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlin
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

public abstract class BaseAndroidPlugin internal constructor(private val pluginId: String) : KradlePlugin() {

    @InternalKradleApi
    override fun configure(project: Project) {
        if (project.isRoot && project.rootProject.subprojects.isNotEmpty()) {
            throw GradleException("Kradle Android plugins can only be applied to subprojects, not the root project")
        }

        val configPlugin = project.plugins.apply(AndroidConfigPlugin::class)

        project.plugins.apply(pluginId)

        project.configureKotlin(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)

        configureAndroid(configPlugin)
    }

    protected open fun configureAndroid(configPlugin: AndroidConfigPlugin) {}
}
