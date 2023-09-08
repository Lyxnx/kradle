package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.dsl.hasPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.api.provider.Property
import javax.inject.Inject

public open class KotlinConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : KradlePlugin() {

    public lateinit var testOptions: KotlinTestOptionsImpl
        private set

    internal val jvmTarget: Property<JavaVersion>
        get() = kradleExtension.jvmTarget

    override fun Project.configure() {
        check(pluginRegistry.hasPlugin("kotlin")) {
            """
            Kotlin gradle plugin cannot be found. Make sure it is added as a build dependency to the project.
            E.g. Add using "apply false" to the root project:
            plugins {
                kotlin("jvm") version "<version>" apply false
            }
            """.trimIndent()
        }

        testOptions = createExtension(KotlinTestOptions.NAME, KotlinTestOptions::class)
    }

}
