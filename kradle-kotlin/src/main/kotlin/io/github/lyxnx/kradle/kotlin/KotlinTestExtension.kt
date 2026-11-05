package io.github.lyxnx.kradle.kotlin

import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.gradle.api.tasks.testing.testng.TestNGOptions

/**
 * Options used to configure tests for the Kotlin gradle plugin
 */
public interface KotlinTestExtension {

    public val testFramework: Property<KotlinTestFramework>

    /**
     * Configures JUnit Platform (Junit 5) that will be used for tests
     */
    public fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit = {})

    /**
     * Configures JUnit 4 for tests
     */
    public fun useJunit(configure: JUnitOptions.() -> Unit = {})

    /**
     * Configures TestNG for tests
     */
    public fun useTestNG(configure: TestNGOptions.() -> Unit = {})

    public companion object {
        public const val NAME: String = "test"
    }
}

/**
 * A kotlin test framework
 */
public enum class KotlinTestFramework {
    /**
     * Modern JUnit (5+)
     */
    JUNIT_PLATFORM,

    /**
     * Older JUnit 4
     *
     * More compatible with Android projects
     */
    JUNIT,

    /**
     * TestNG framework
     */
    TESTNG
}
