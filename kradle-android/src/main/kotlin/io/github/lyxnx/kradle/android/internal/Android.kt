package io.github.lyxnx.kradle.android.internal

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import io.github.lyxnx.kradle.android.AndroidOptions
import io.github.lyxnx.kradle.kotlin.KotlinTestOptions
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByName

internal typealias AndroidCommonExtension = CommonExtension<*, *, *, *, *, *>

internal fun <T : AndroidCommonExtension> Project.android(configure: T.() -> Unit) {
    extensions.configure("android", configure)
}

@JvmName("androidComponentsCommon")
internal fun Project.androidComponents(configure: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    androidComponents<AndroidComponentsExtension<*, *, *>>(configure)
}

internal fun <T : AndroidComponentsExtension<*, *, *>> Project.androidComponents(configure: T.() -> Unit) {
    extensions.configure("androidComponents", configure)
}

internal val AndroidOptions.test: KotlinTestOptions
    get() = (this as ExtensionAware).extensions.getByName<KotlinTestOptions>(KotlinTestOptions.NAME)
