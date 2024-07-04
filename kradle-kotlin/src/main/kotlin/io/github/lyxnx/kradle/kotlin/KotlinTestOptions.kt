package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.setFinalValue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.gradle.kotlin.dsl.property

/**
 * Options used to configure tests for the Kotlin gradle plugin
 */
public open class KotlinTestOptions(project: Project) : ExtensionDefaults<KotlinTestOptions> {

    /**
     * Whether the JUnit Jupiter Platform should be used
     */
    internal val useJunitPlatform: Property<Boolean> = project.objects.property()

    internal val configuration: Property<TestFrameworkOptions.() -> Unit> = project.objects.property()

    init {
        useJunitPlatform
            .convention(false)
            .finalizeValueOnRead()
        configuration
            .convention { }
            .finalizeValueOnRead()
    }

    /**
     * Configures JUnit Platform (Junit 5) that will be used for tests
     */
    public fun useJunitPlatform(configure: Action<JUnitPlatformOptions>? = null) {
        useJunitPlatform.setFinalValue(true)
        configuration.set { configure?.execute(this as JUnitPlatformOptions) }
    }

    /**
     * Configures JUnit 4 for tests
     */
    public fun useJunit(configure: Action<JUnitOptions>? = null) {
        useJunitPlatform.setFinalValue(false)
        configuration.set { configure?.execute(this as JUnitOptions) }
    }

    override fun setDefaults(defaults: KotlinTestOptions) {
        useJunitPlatform.convention(defaults.useJunitPlatform)
        configuration.convention(defaults.configuration)
    }

    public companion object {
        public const val NAME: String = "test"
    }
}
