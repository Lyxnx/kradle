package io.github.lyxnx.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.provider.Property

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

    public companion object {
        public const val NAME: String = "kradle"
    }

}