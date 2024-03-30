package io.github.lyxnx.gradle.kotlin.internal

import io.github.lyxnx.gradle.dsl.getOrElse
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal fun Project.getJavaVersion(javaVersion: Provider<JavaVersion>) = javaVersion.getOrElse {
    JavaVersion.current().also {
        logger.warn("Could not determine specified JVM target - defaulting to current JDK version ($it)")
    }
}
