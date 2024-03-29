package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.dsl.hasPlugin
import io.github.lyxnx.gradle.kotlin.KotlinConfigPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import javax.inject.Inject
import kotlin.properties.Delegates

internal const val CACHE_FIX_PLUGIN = "org.gradle.android.cache-fix"

public open class AndroidConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry
) : KradlePlugin() {

    internal lateinit var androidOptions: AndroidOptionsImpl
        private set

    internal val jvmTarget: Provider<JavaVersion>
        get() = kradleExtension.jvmTarget

    internal var hasCacheFixPlugin: Boolean by Delegates.notNull()
        private set

    override fun Project.configure() {
        check(pluginRegistry.hasPlugin("com.android.application")) {
            """
            Android gradle plugin cannot be found. Make sure it is added as a build dependency to the project.
            E.g. Add using "apply false" to the root project:
            plugins {
                id("com.android.application") version "<version>" apply false
            }
            """.trimIndent()
        }

        hasCacheFixPlugin = pluginRegistry.hasPlugin(CACHE_FIX_PLUGIN)

        val kotlinConfig = plugins.apply(KotlinConfigPlugin::class)
        androidOptions = createExtension(AndroidOptions.NAME)
        androidOptions.setTestDefaults(kotlinConfig.testOptions)
    }
}
