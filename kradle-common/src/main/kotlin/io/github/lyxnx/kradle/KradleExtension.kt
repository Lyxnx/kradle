package io.github.lyxnx.kradle

import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
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
public interface KradleExtension : ExtensionAware {

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
