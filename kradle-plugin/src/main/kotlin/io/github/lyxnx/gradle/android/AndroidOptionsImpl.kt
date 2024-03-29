package io.github.lyxnx.gradle.android

import io.github.lyxnx.gradle.ExtensionDefaults
import io.github.lyxnx.gradle.kotlin.KotlinTestOptions
import io.github.lyxnx.gradle.kotlin.KotlinTestOptionsImpl
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.create

@Suppress("LeakingThis")
internal abstract class AndroidOptionsImpl : AndroidOptions, ExtensionDefaults<AndroidOptionsImpl> {

    private val testOptions: KotlinTestOptionsImpl
    private var areDefaultsSet = false

    init {
        minSdk
            .convention(AndroidOptions.DEFAULT_MIN_SDK)
            .finalizeValueOnRead()
        targetSdk
            .convention(AndroidOptions.DEFAULT_TARGET_SDK)
            .finalizeValueOnRead()
        compileSdk
            .convention(targetSdk.map(Int::toString))
            .finalizeValueOnRead()
        testTasksFilter
            .convention { it.name.endsWith("ReleaseUnitTest") }
            .finalizeValueOnRead()

        testOptions = (this as ExtensionAware).extensions
            .create(
                publicType = KotlinTestOptions::class,
                name = KotlinTestOptions.NAME,
                instanceType = KotlinTestOptionsImpl::class
            ) as KotlinTestOptionsImpl
    }

    override fun setDefaults(defaults: AndroidOptionsImpl) {
        minSdk.convention(defaults.minSdk)
        targetSdk.convention(defaults.targetSdk)
        compileSdk.convention(defaults.compileSdk)
        testTasksFilter.convention(defaults.testTasksFilter)
        setTestDefaults(defaults.testOptions)
    }

    internal fun setTestDefaults(defaults: KotlinTestOptionsImpl) {
        if (areDefaultsSet) return
        areDefaultsSet = true
        testOptions.setDefaults(defaults)
    }

}
