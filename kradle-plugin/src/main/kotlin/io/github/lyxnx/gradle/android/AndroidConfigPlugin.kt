package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.dsl.findBooleanProperty
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
private const val PROP_SUPPRESS_CACHEFIX = "kradle.android.suppressCacheFixWarning"

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
        val suppressWarning = findBooleanProperty(PROP_SUPPRESS_CACHEFIX) ?: false

        if (!suppressWarning && !hasCacheFixPlugin) {
            logger.warn(
                """
                    Could not find Android cache fix plugin. It is recommended to apply this plugin, but not required.
                    (See https://github.com/gradle/android-cache-fix-gradle-plugin)
                
                    If it would like to be used, make sure it is added as a build dependency
                    E.g. Add using apply false to the root project:
                    plugins {
                        id("$CACHE_FIX_PLUGIN") version "<version>" apply false
                    }
                
                    Alternatively, silence this warning by setting the property '$PROP_SUPPRESS_CACHEFIX' to truesdf
                """.trimIndent()
            )
        }

        val kotlinConfig = plugins.apply(KotlinConfigPlugin::class)
        androidOptions = createExtension(AndroidOptions.NAME)
        androidOptions.setTestDefaults(kotlinConfig.testOptions)
    }
}
