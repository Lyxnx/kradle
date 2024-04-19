package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.dsl.hasPlugin
import io.github.lyxnx.gradle.internal.KOTLIN_PLUGIN_ID
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import javax.inject.Inject

public open class KotlinConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : KradlePlugin() {

    internal lateinit var testOptions: DefaultKotlinTestOptions
        private set

    override fun Project.configure() {
        check(pluginRegistry.hasPlugin(KOTLIN_PLUGIN_ID)) {
            """
            Kotlin gradle plugin cannot be found. Make sure it is added as a build dependency to the project.
            E.g. Add using "apply false" to the root project:
            plugins {
                kotlin("jvm") version "<version>" apply false
            }
            """.trimIndent()
        }

        testOptions = createExtension(KotlinTestOptions.NAME, publicType = KotlinTestOptions::class)
    }

}
