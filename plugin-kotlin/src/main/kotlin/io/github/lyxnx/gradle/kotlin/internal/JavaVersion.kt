package io.github.lyxnx.gradle.kotlin.internal

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal fun JavaVersion.toJvmTarget(): JvmTarget? {
    return try {
         JvmTarget.fromTarget(toString())
    } catch (e: IllegalArgumentException) {
        null
    }
}
