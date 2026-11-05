package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.CompileSdkSpec
import com.android.build.api.dsl.TargetSdkSpec
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider

/**
 * Configuration options for Android projects
 */
public interface AndroidExtension : ExtensionAware {

    /**
     * Configures the min sdk for all Android modules
     */
    public val minSdk: Property<Int>

    /**
     * Configures the target sdk for all Android modules
     */
    public val targetSdk: Property<Int>

    /**
     * Configures the target sdk for all Android modules
     */
    public fun targetSdk(block: TargetSdkSpec.() -> Unit)

    /**
     * Configures the compile sdk for all Android modules
     *
     * If not configured, [targetSdk] will be used for the compile sdk value
     */
    public val compileSdk: Property<Int>

    /**
     * Configure the compile sdk for all Android modules
     */
    public fun compileSdk(block: CompileSdkSpec.() -> Unit)

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
     * This will be called for each task and if true is returned, that task will be included when running the
     * main `test` command, and excluded otherwise
     *
     * By default, all test tasks will be run
     */
    public val testTasksFilter: Property<(TaskProvider<*>) -> Boolean>

    public companion object {
        public const val NAME: String = "android"
    }

}
