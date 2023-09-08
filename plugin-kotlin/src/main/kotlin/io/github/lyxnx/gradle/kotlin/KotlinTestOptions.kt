package io.github.lyxnx.gradle.kotlin

import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

/**
 * Options used to configure tests for the Kotlin gradle plugin
 */
public interface KotlinTestOptions {

    /**
     * Whether the JUnit Jupiter Platform should be used
     */
    public val useJunitPlatform: Property<Boolean>

    /**
     * Configures JUnit Platform (Junit 5) that will be used for tests
     */
    public fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit = {})

    /**
     * Configures JUnit 4 for tests
     */
    public fun useJunit(configure: JUnitOptions.() -> Unit = {})

    public companion object {
        public const val NAME: String = "test"
    }

}
