package io.github.lyxnx.gradle.android

import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider

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

    public val testTasksFilter: Property<(TaskProvider<*>) -> Boolean>

    public companion object {
        public const val DEFAULT_MIN_SDK: Int = 21
        public const val DEFAULT_TARGET_SDK: Int = 34

        public const val NAME: String = "android"
    }

}
