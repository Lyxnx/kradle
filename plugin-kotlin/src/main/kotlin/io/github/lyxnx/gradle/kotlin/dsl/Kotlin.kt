package io.github.lyxnx.gradle.kotlin.dsl

import io.github.lyxnx.gradle.KradlePlugin
import io.github.lyxnx.gradle.kotlin.internal.toJvmTarget
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures all tasks with the type [KotlinCompile]
 */
public inline fun Project.kotlinCompile(crossinline configure: KotlinCompile.() -> Unit) {
    tasks.withType<KotlinCompile>().configureEach { configure() }
}

public fun KradlePlugin.configureKotlin(javaVersion: Provider<JavaVersion>) {
    project.kotlinCompile {
        compilerOptions {
            jvmTarget.set(
                javaVersion.get().let { ver ->
                    ver.toJvmTarget() ?: JvmTarget.JVM_1_8.also {
                        this@configureKotlin.logger.warn("Could not resolve JVM target from java version: $ver, falling back to ${JvmTarget.JVM_1_8}")
                    }
                }
            )
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}
