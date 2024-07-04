package io.github.lyxnx.kradle.android

internal object Constants {
    const val MIN_SDK = 21
    const val TARGET_SDK = 34

    const val ANDROIDX_TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"

    const val AGP_PLUGIN_DEP = "com.android.tools.build:gradle"
    const val LIBRARY_PLUGIN_ID = "com.android.library"
    const val APPLICATION_PLUGIN_ID = "com.android.application"

    const val CACHEFIX_PLUGIN_ID = "org.gradle.android.cache-fix"
    const val CACHEFIX_PLUGIN_DEP = "org.gradle.android.cache-fix:org.gradle.android.cache-fix.gradle.plugin"

    const val KOTLIN_ANDROID_PLUGIN_ID = "kotlin-android"

    const val PROP_ANDROID_SUPPRESS_CACHEFIX = "kradle.android.suppressCacheFixWarning"
}
