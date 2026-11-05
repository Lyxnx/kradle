package io.github.lyxnx.kradle.android

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.dsl.hasPlugin
import io.github.lyxnx.kradle.kotlin.KotlinConfigPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import javax.inject.Inject

public open class AndroidConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry
) : KradlePlugin() {

    internal lateinit var androidExtension: AndroidExtensionImpl
        private set

    @InternalKradleApi
    override fun configure(project: Project) {
        check(
            pluginRegistry.hasPlugin(Constants.APPLICATION_PLUGIN_ID)
                || pluginRegistry.hasPlugin(Constants.LIBRARY_PLUGIN_ID)
                || pluginRegistry.hasPlugin(Constants.KMP_LIBRARY_PLUGIN_ID)
        ) {
            """
            Android gradle plugin cannot be found. Make sure it is added as a build dependency to the project:
            1. Add using "apply false" to the root project:
               plugins {
                   id("${Constants.APPLICATION_PLUGIN_ID}") version <version> apply false
                   // or
                   id("${Constants.LIBRARY_PLUGIN_ID}") version <version> apply false
               }
            2. Add as dependency to buildSrc or another included build:
               dependencies {
                   implementation("${Constants.AGP_PLUGIN_DEP}:<version>")
               }
            """.trimIndent()
        }

        val kotlinConfig = project.plugins.apply(KotlinConfigPlugin::class)
        androidExtension = createExtension(AndroidExtension.NAME, publicType = AndroidExtension::class)
        androidExtension.setTestDefaults(kotlinConfig.testOptions)
    }
}
