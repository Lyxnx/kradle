package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.hasPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import javax.inject.Inject

public open class KotlinConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : KradlePlugin() {

    public lateinit var testOptions: KotlinTestOptions
        private set

    override fun Project.configure() {
        check(pluginRegistry.hasPlugin(Constants.KOTLIN_PLUGIN_ID)) {
            """
            Kotlin gradle plugin cannot be found. Make sure it is added as a build dependency to the project:
            1. Add using "apply false" to the root project:
               plugins {
                   kotlin("jvm") version <version> apply false
               }
            2. Add as dependency to buildSrc or another included build:
               dependencies {
                   implementation(kotlin("gradle-plugin", version = <version>))
               }
            """.trimIndent()
        }

        testOptions = createExtension(KotlinTestOptions.NAME)
    }

}
