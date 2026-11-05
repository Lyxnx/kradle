package io.github.lyxnx.kradle.kotlin

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.dsl.setFinalValue
import org.gradle.api.provider.Property
import org.gradle.api.tasks.testing.TestFrameworkOptions
import org.gradle.api.tasks.testing.junit.JUnitOptions
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions
import org.gradle.api.tasks.testing.testng.TestNGOptions

@InternalKradleApi
public abstract class KotlinTestExtensionImpl : KotlinTestExtension, ExtensionDefaults<KotlinTestExtensionImpl> {

    abstract override val testFramework: Property<KotlinTestFramework>

    internal abstract val configuration: Property<TestFrameworkOptions.() -> Unit>

    init {
        testFramework
            .convention(KotlinTestFramework.JUNIT_PLATFORM)
            .finalizeValueOnRead()
        configuration
            .convention { }
            .finalizeValueOnRead()
    }

    override fun useJunitPlatform(configure: JUnitPlatformOptions.() -> Unit) {
        testFramework.setFinalValue(KotlinTestFramework.JUNIT_PLATFORM)
        configuration.set { (this as JUnitPlatformOptions).configure() }
    }

    override fun useJunit(configure: JUnitOptions.() -> Unit) {
        testFramework.setFinalValue(KotlinTestFramework.JUNIT)
        configuration.set { (this as JUnitOptions).configure() }
    }

    override fun useTestNG(configure: TestNGOptions.() -> Unit) {
        testFramework.setFinalValue(KotlinTestFramework.TESTNG)
        configuration.set { (this as TestNGOptions).configure() }
    }

    override fun setDefaults(defaults: KotlinTestExtensionImpl) {
        testFramework.convention(defaults.testFramework)
        configuration.convention(defaults.configuration)
    }
}
