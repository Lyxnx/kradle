package io.github.lyxnx.gradle

import io.github.lyxnx.gradle.dsl.findByName
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/**
 * Extension used as a namespace for all Kradle plugins' extensions
 *
 * Eg all plugins with custom extensions will be scoped to this:
 * ```
 * kradle {
 *     otherPlugin {
 *
 *     }
 * }
 * ```
 */
public interface KradleExtension {

    /**
     * JVM version to apply to all modules
     */
    public val jvmTarget: Property<JavaVersion>

    /**
     * Extra compiler arguments to apply to all modules
     */
    public val kotlinCompilerArgs: SetProperty<String>

    public companion object {
        public const val NAME: String = "kradle"
    }

}

@Suppress("LeakingThis")
internal abstract class DefaultKradleExtension : KradleExtension, ExtensionAware, ExtensionDefaults<DefaultKradleExtension> {

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_11)
            .finalizeValueOnRead()

        kotlinCompilerArgs
            .convention(emptySet())
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: DefaultKradleExtension) {
        jvmTarget.convention(defaults.jvmTarget)
        kotlinCompilerArgs.convention(defaults.kotlinCompilerArgs)
    }
}


@PublishedApi
internal val ExtensionContainer.kradle: DefaultKradleExtension?
    get() = findByName<DefaultKradleExtension>(KradleExtension.NAME)

