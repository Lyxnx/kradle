package io.github.lyxnx.gradle.kotlin

import io.github.lyxnx.gradle.ExtensionDefaults
import io.github.lyxnx.gradle.dsl.setFinalValue
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

@Suppress("LeakingThis")
public abstract class KotlinTestOptionsImpl : KotlinTestOptions,
    ExtensionDefaults<KotlinTestOptionsImpl> {

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

    override fun setDefaults(defaults: KotlinTestOptionsImpl) {
        useJunitPlatform.convention(defaults.useJunitPlatform)
        configuration.convention(defaults.configuration)
    }

}
