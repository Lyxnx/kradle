package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.CompileSdkSpec
import com.android.build.api.dsl.TargetSdkSpec
import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.InternalKradleApi
import io.github.lyxnx.kradle.kotlin.KotlinTestExtension
import io.github.lyxnx.kradle.kotlin.KotlinTestExtensionImpl
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

@OptIn(InternalKradleApi::class)
internal abstract class AndroidExtensionImpl @Inject constructor(providers: ProviderFactory) : AndroidExtension,
    ExtensionDefaults<AndroidExtensionImpl> {

    private val testOptions: KotlinTestExtensionImpl
    private var testDefaultsSet = false

    internal abstract val configureTargetSdk: Property<TargetSdkSpec.() -> Unit>
    internal abstract val configureCompileSdk: Property<CompileSdkSpec.() -> Unit>

    init {
        minSdk.finalizeValueOnRead()
        targetSdk.finalizeValueOnRead()
        configureTargetSdk.finalizeValueOnRead()
        compileSdk.finalizeValueOnRead()
        configureCompileSdk.finalizeValueOnRead()
        buildToolsVersion.finalizeValueOnRead()
        ndkVersion.finalizeValueOnRead()
        testTasksFilter
            .convention { true }
            .finalizeValueOnRead()

        testOptions = extensions.create(
            name = KotlinTestExtension.NAME,
            publicType = KotlinTestExtension::class,
            instanceType = KotlinTestExtensionImpl::class
        ) as KotlinTestExtensionImpl
    }

    override fun targetSdk(block: TargetSdkSpec.() -> Unit) {
        configureTargetSdk.set(block)
    }

    override fun compileSdk(block: CompileSdkSpec.() -> Unit) {
        configureCompileSdk.set(block)
    }

    override fun setDefaults(defaults: AndroidExtensionImpl) {
        minSdk.convention(defaults.minSdk)
        targetSdk.convention(defaults.targetSdk)
        configureTargetSdk.convention(defaults.configureTargetSdk)
        configureCompileSdk.convention(defaults.configureCompileSdk)
        compileSdk.convention(defaults.compileSdk)
        buildToolsVersion.convention(defaults.buildToolsVersion)
        ndkVersion.convention(defaults.ndkVersion)
        testTasksFilter.convention(defaults.testTasksFilter)
        setTestDefaults(defaults.testOptions)
    }

    internal fun setTestDefaults(defaults: KotlinTestExtensionImpl) {
        if (testDefaultsSet) return
        testDefaultsSet = true
        testOptions.setDefaults(defaults)
    }
}
