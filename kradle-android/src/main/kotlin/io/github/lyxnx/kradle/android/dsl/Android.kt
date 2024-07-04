package io.github.lyxnx.kradle.android.dsl

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import io.github.lyxnx.kradle.android.AndroidOptions
import io.github.lyxnx.kradle.kotlin.KotlinTestOptions
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByName

public typealias AndroidCommonExtension = CommonExtension<*, *, *, *, *, *>

/**
 * Configures the `android` extension of type T
 *
 * Eg:
 *
 * ```kotlin
 * android<LibraryExtension> {
 *     // library specific configuration
 * }
 * android<ApplicationExtension> {
 *     // application specific configuration
 * }
 * ```
 */
public fun <T : AndroidCommonExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

/**
 * Configures the generic `androidComponents` extension
 */
@JvmName("androidComponentsCommon")
public fun Project.androidComponents(configure: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    androidComponents<AndroidComponentsExtension<*, *, *>>(configure)
}

/**
 * Configures the `androidComponents` extension of type T
 */
public fun <T : AndroidComponentsExtension<*, *, *>> Project.androidComponents(configure: T.() -> Unit) {
    extensions.configure("androidComponents", configure)
}

internal val AndroidOptions.test: KotlinTestOptions
    get() = (this as ExtensionAware).extensions.getByName<KotlinTestOptions>(KotlinTestOptions.NAME)
