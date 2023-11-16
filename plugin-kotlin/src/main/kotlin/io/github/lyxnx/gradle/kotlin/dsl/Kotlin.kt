package io.github.lyxnx.gradle.kotlin.dsl

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.kotlin.internal.kotlin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures all tasks with the type [KotlinCompile]
 */
public inline fun Project.kotlinCompile(crossinline configure: KotlinCompile.() -> Unit) {
    tasks.withType<KotlinCompile>().configureEach { configure() }
}

/**
 * Configures kotlin for the project
 *
 * This will set the JVM toolchain, which will default to the current JVM version if not specified or invalid
 *
 * This will also add the `kotlin.RequiresOptIn` opt-in annotation to the compiler options
 *
 * @param javaVersion JVM toolchain version
 */
public fun KradlePlugin.configureKotlin(javaVersion: Provider<JavaVersion>) {
    project.kotlin {
        jvmToolchain(javaVersion.getOrElse(
            JavaVersion.current().also {
                logger.warn("Could not determine specified JVM target - defaulting to current JDK version ($it)")
            }
        ).majorVersion.toInt())
    }

    project.kotlinCompile {
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}
