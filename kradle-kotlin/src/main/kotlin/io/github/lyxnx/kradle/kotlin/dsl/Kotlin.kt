package io.github.lyxnx.kradle.kotlin.dsl

import io.github.lyxnx.kradle.kotlin.DefaultKotlinTestOptions
import io.github.lyxnx.kradle.kotlin.KotlinTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

public fun Project.configureKotlinPlugin(
    plugin: KotlinBasePluginWrapper,
    javaVersion: Provider<JavaVersion>,
    compilerArgs: Provider<Set<String>>
) {
    afterEvaluate {
        if (plugin is KotlinMultiplatformPluginWrapper) {
            extensions.getByType<KotlinMultiplatformExtension>().apply {
                targets.withType<KotlinJvmTarget>().configureEach {
                    compilations.configureEach {
                        compileJavaTaskProvider?.configure {
                            targetCompatibility = javaVersion.get().toString()
                            sourceCompatibility = javaVersion.get().toString()
                        }
                        compileTaskProvider.configure {
                            compilerOptions.applyKotlinOptions(javaVersion, compilerArgs)
                        }
                    }
                }

                // Configures KMP targets to allow expect/actual classes that are not stable
                // Remove when https://kotlinlang.org/docs/components-stability.html#current-stability-of-kotlin-components shows as stable
                // https://youtrack.jetbrains.com/issue/KT-61573
                targets.all(closureOf<KotlinTarget> {
                    compilations.configureEach {
                        compileTaskProvider.configure {
                            compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                })
            }
        } else {
            tasks.withType<KotlinJvmCompile>().configureEach {
                compilerOptions.applyKotlinOptions(javaVersion, compilerArgs)
            }
        }
    }
}

public fun Project.configureJavaPlugin(javaVersion: Provider<JavaVersion>) {
    afterEvaluate {
        extensions.getByType<JavaPluginExtension>().apply {
            sourceCompatibility = javaVersion.get()
            targetCompatibility = javaVersion.get()
            disableJava8ObsoleteWarnings()
        }
    }
}

private fun Project.disableJava8ObsoleteWarnings() {
    afterEvaluate {
        tasks.withType<JavaCompile>().configureEach {
            // JDK 21 considers java 8 source obsolete
            options.compilerArgs.add("-Xlint:-options")
        }
    }
}

public fun KotlinJvmCompilerOptions.applyKotlinOptions(
    javaVersion: Provider<JavaVersion>,
    compilerArgs: Provider<Set<String>>
) {
    jvmTarget.set(javaVersion.map { it.toJvmTarget() })
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    freeCompilerArgs.addAll(compilerArgs.get())
}

private fun JavaVersion.toJvmTarget(): JvmTarget = JvmTarget.fromTarget(toString())

public fun Project.configureKotlinTest(options: KotlinTestOptions) {
    tasks.withType<Test>().configureEach {
        setTestOptions(options)
    }
}

public fun Test.setTestOptions(options: KotlinTestOptions) {
    val configure = (options as? DefaultKotlinTestOptions)?.configuration?.get()
    if (options.useJunitPlatform.get()) {
        configure?.let { useJUnitPlatform(it) }
        testLogging { events("passed", "skipped", "failed") }
    } else {
        configure?.let { useJUnit(it) }
    }
}
