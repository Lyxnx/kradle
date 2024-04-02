package io.github.lyxnx.gradle.kotlin.dsl

import io.github.lyxnx.gradle.kotlin.KotlinTestOptions
import io.github.lyxnx.gradle.kotlin.KotlinTestOptionsImpl
import io.github.lyxnx.gradle.kotlin.internal.getJavaVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures kotlin for the project
 *
 * This will set the JVM toolchain, which will default to the current JVM version if not specified or invalid
 *
 * This will also add the `kotlin.RequiresOptIn` opt-in annotation to the compiler options
 *
 * @param javaVersion JVM toolchain version
 */
public fun Project.configureKotlin(javaVersion: Provider<JavaVersion>) {
    dependencies {
        add("testImplementation", kotlin("test"))
    }

    extensions.getByType<JavaPluginExtension>().apply {
        getJavaVersion(javaVersion).let {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(getJavaVersion(javaVersion).toString()))
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}

public fun Test.setTestOptions(options: KotlinTestOptions) {
    val configure = (options as KotlinTestOptionsImpl).configuration.get()
    if (options.useJunitPlatform.get()) {
        useJUnitPlatform(configure)
        testLogging { events("passed", "skipped", "failed") }
    } else {
        useJUnit(configure)
    }
}
