package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.setFinalValue
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

/**
 * Options used to configure tests for the Kotlin gradle plugin
 */
public interface KotlinTestOptions {

    /**
     * Whether the JUnit Jupiter Platform should be used
     */
    public val useJunitPlatform: Provider<Boolean>

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

@Suppress("LeakingThis")
public abstract class DefaultKotlinTestOptions : KotlinTestOptions, ExtensionDefaults<DefaultKotlinTestOptions> {

    abstract override val useJunitPlatform: Property<Boolean>

    internal abstract val configuration: Property<TestFrameworkOptions.() -> Unit>

    init {
        useJunitPlatform
            .convention(true)
            .finalizeValueOnRead()

        configuration
            .convention { }
            .finalizeValueOnRead()
    }

    override fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit) {
        useJunitPlatform.setFinalValue(true)
        configuration.set { (this as JUnitPlatformOptions).configure() }
    }

    override fun useJunit(configure: JUnitOptions.() -> Unit) {
        useJunitPlatform.setFinalValue(false)
        configuration.set { (this as JUnitOptions).configure() }
    }

    override fun setDefaults(defaults: DefaultKotlinTestOptions) {
        useJunitPlatform.convention(defaults.useJunitPlatform)
        configuration.convention(defaults.configuration)
    }

}
