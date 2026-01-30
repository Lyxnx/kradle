package io.github.lyxnx.kradle.kotlin.dsl

import io.github.lyxnx.kradle.kotlin.KotlinTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

public fun Project.configureKotlin(
    javaVersion: Provider<JavaVersion>,
    compilerArgs: Provider<Set<String>>
) {
    extensions.configure<KotlinProjectExtension> {
        jvmToolchain {
            languageVersion.set(javaVersion.map { JavaLanguageVersion.of(it.majorVersion) })
        }

        if (this is HasConfigurableKotlinCompilerOptions<*>) {
            compilerOptions {
                freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")

                if (this@configure is KotlinMultiplatformExtension) {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }

                freeCompilerArgs.addAll(compilerArgs)
            }
        }
    }
}

public fun Project.configureJava() {
    // source/target compatibility is set by kotlin plugin
    tasks.withType<JavaCompile>().configureEach {
        // JDK 21 considers java 8 source obsolete
        options.compilerArgs.add("-Xlint:-options")
    }
}

public fun Project.configureKotlinTest(options: KotlinTestOptions) {
    tasks.withType<Test>().configureEach {
        setTestOptions(options)
    }
}

public fun Test.setTestOptions(options: KotlinTestOptions) {
    val configure = options.configuration.get()
    if (options.useJunitPlatform.get()) {
        useJUnitPlatform(configure)
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events(STARTED, PASSED, SKIPPED, FAILED, STANDARD_ERROR, STANDARD_OUT)
            showStandardStreams = true
            showStackTraces = true
        }
    } else {
        useJUnit(configure)
    }
}
