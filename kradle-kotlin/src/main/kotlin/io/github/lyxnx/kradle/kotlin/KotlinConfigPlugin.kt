package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.hasPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import javax.inject.Inject

internal const val KOTLIN_PLUGIN_ID = "kotlin"

public open class KotlinConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : KradlePlugin() {

    public lateinit var testOptions: DefaultKotlinTestOptions
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
