package io.github.lyxnx.kradle.android

import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.dsl.findBooleanProperty
import io.github.lyxnx.kradle.dsl.hasPlugin
import io.github.lyxnx.kradle.kotlin.KotlinConfigPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import javax.inject.Inject
import kotlin.properties.Delegates

public open class AndroidConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry
) : KradlePlugin() {

    private lateinit var _androidOptions: DefaultAndroidOptions
    public val androidOptions: AndroidOptions get() = _androidOptions

    internal var hasCacheFixPlugin: Boolean by Delegates.notNull()
        private set

    override fun Project.configure() {
        check(pluginRegistry.hasPlugin(ANDROID_APPLICATION_PLUGIN_ID)) {
            """
            Android gradle plugin cannot be found. Make sure it is added as a build dependency to the project.
            E.g. Add using "apply false" to the root project:
            plugins {
                id("$ANDROID_APPLICATION_PLUGIN_ID") version "<version>" apply false
            }
            """.trimIndent()
        }

        hasCacheFixPlugin = pluginRegistry.hasPlugin(ANDROID_CACHE_FIX_PLUGIN_ID)
        val suppressWarning = findBooleanProperty(PROP_ANDROID_SUPPRESS_CACHEFIX) ?: false

        if (!suppressWarning && !hasCacheFixPlugin) {
            logger.warn(
                """
                    Could not find Android cache fix plugin. It is recommended to apply this plugin, but not required.
                    (See https://github.com/gradle/android-cache-fix-gradle-plugin)
                
                    If it would like to be used, make sure it is added as a build dependency
                    E.g. Add using apply false to the root project:
                    plugins {
                        id("$ANDROID_CACHE_FIX_PLUGIN_ID") version "<version>" apply false
                    }
                
                    Alternatively, silence this warning by setting the property '$PROP_ANDROID_SUPPRESS_CACHEFIX' to true
                """.trimIndent()
            )
        }

        val kotlinConfig = plugins.apply(KotlinConfigPlugin::class)
        _androidOptions = createExtension(AndroidOptions.NAME)
        _androidOptions.setTestDefaults(kotlinConfig.testOptions)
    }
}
