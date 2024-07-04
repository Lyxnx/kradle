package io.github.lyxnx.kradle

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.createWithDefaults
import io.github.lyxnx.kradle.dsl.findByName
import io.github.lyxnx.kradle.dsl.parents
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

/**
 * Base Kradle plugin
 *
 * Extending this makes it possible to scope gradle extensions to Kradle plugins only
 */
public abstract class KradlePlugin : Plugin<Project> {

    public lateinit var project: Project
        private set

    protected lateinit var kradleExtension: KradleExtension
        private set

    @PublishedApi
    internal val kradleExtensions: Sequence<KradleExtension>
        get() = project.parents.mapNotNull { it.extensions.kradle }

    final override fun apply(target: Project) {
        project = target
        kradleExtension = project.extensions.obtainKradleExtension()
        target.configure()
    }

    /**
     * Performs plugin configuration
     */
    protected abstract fun Project.configure()

    /**
     * Create an extension with [name] in the Kradle namespace and returns it
     *
     * Eg
     * ```
     * val ext: SomeExtension = createExtension("someExtension")
     * ```
     *
     * would then be referenced like so
     *
     * ```
     * kradle {
     *     someExtension {
     *     }
     * }
     * ```
     */
    protected inline fun <reified T : ExtensionDefaults<T>> createExtension(
        name: String,
        publicType: KClass<in T>? = null
    ): T {
        val defaults = kradleExtensions
            .mapNotNull { it.extensions.findByName<T>(name) }
            .firstOrNull()
        return kradleExtension.extensions.createWithDefaults(name, defaults, publicType)
    }

    private fun ExtensionContainer.obtainKradleExtension(): KradleExtension {
        return kradle ?: createWithDefaults(KradleExtension.NAME, kradleExtensions.firstOrNull())
    }
}
