package io.github.lyxnx.kradle.android

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.kotlin.DefaultKotlinTestOptions
import io.github.lyxnx.kradle.kotlin.KotlinTestOptions
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create

/**
 * Configuration options for Android projects
 */
public interface AndroidOptions {

    /**
     * Configures the min sdk for all Android modules
     */
    public val minSdk: Property<Int>

    /**
     * Configures the target sdk for all Android modules
     */
    public val targetSdk: Property<Int>

    /**
     * Configures the compile sdk for all Android modules
     *
     * This can be a version number "33" or a version code "TIRAMISU"
     *
     * Will use [targetSdk] if not configured
     */
    public val compileSdk: Property<String>

    /**
     * Configures the build tools property for all Android modules
     *
     * If not configured, the default version for the given AGP version will be used
     *
     * See https://developer.android.com/build/releases/gradle-plugin#compatibility for the default version
     */
    public val buildToolsVersion: Property<String>

    /**
     * Configures the NDK version for all Android modules
     *
     * If not configured, the default version for the given AGP version will be used
     *
     * See https://developer.android.com/build/releases/gradle-plugin#compatibility for the default version
     */
    public val ndkVersion: Property<String>

    /**
     * Configures the filter for tasks named `test`
     *
     * This will be called for each task and if true is returned, that that task will be included when running the
     * main `test` command, and excluded otherwise
     *
     * By default, all test tasks will be run
     */
    public val testTasksFilter: Property<(TaskProvider<*>) -> Boolean>

    public companion object {
        /**
         * The default minimum SDK for all Android modules
         */
        public const val DEFAULT_MIN_SDK: Int = 21

        /**
         * The default target SDK for all Android modules
         */
        public const val DEFAULT_TARGET_SDK: Int = 34

        public const val NAME: String = "android"
    }

}

@Suppress("LeakingThis")
public abstract class DefaultAndroidOptions : AndroidOptions, ExtensionDefaults<DefaultAndroidOptions> {

    private val testOptions: DefaultKotlinTestOptions
    private var testDefaultsSet = false

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
        buildToolsVersion
            .finalizeValueOnRead()
        ndkVersion
            .finalizeValueOnRead()
        testTasksFilter
            .convention { true }
            .finalizeValueOnRead()

        testOptions = (this as ExtensionAware).extensions
            .create(
                publicType = KotlinTestOptions::class,
                name = KotlinTestOptions.NAME,
                instanceType = DefaultKotlinTestOptions::class
            ) as DefaultKotlinTestOptions
    }

    override fun setDefaults(defaults: DefaultAndroidOptions) {
        minSdk.convention(defaults.minSdk)
        targetSdk.convention(defaults.targetSdk)
        compileSdk.convention(defaults.compileSdk)
        buildToolsVersion.convention(defaults.buildToolsVersion)
        ndkVersion.convention(defaults.ndkVersion)
        testTasksFilter.convention(defaults.testTasksFilter)
        setTestDefaults(defaults.testOptions)
    }

    internal fun setTestDefaults(defaults: DefaultKotlinTestOptions) {
        if (testDefaultsSet) return
        testDefaultsSet = true
        testOptions.setDefaults(defaults)
    }

}
