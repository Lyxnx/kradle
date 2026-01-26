@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.kradle.android

import com.android.build.gradle.BasePlugin
import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.isRoot
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlin
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getPlugin
import kotlin.reflect.KClass

public abstract class BaseAndroidPlugin : KradlePlugin() {

    protected val configPlugin: AndroidConfigPlugin
        get() = project.plugins.getPlugin(AndroidConfigPlugin::class)

    protected abstract val pluginClass: KClass<out BasePlugin>

    override fun configure(project: Project) {
        if (project.isRoot && project.rootProject.subprojects.isNotEmpty()) {
            throw GradleException("Kradle Android plugins can only be applied to subprojects, not the root project")
        }

        val configPlugin = project.plugins.apply(AndroidConfigPlugin::class)

        project.plugins.apply(pluginClass)

        project.configureKotlin(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)

        configureAndroid(configPlugin.androidOptions)
    }

    protected open fun configureAndroid(options: AndroidOptions) {}
}
