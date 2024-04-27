package io.github.lyxnx.gradle.kotlin.dsl

import io.github.lyxnx.gradle.kotlin.DefaultKotlinTestOptions
import io.github.lyxnx.gradle.kotlin.KotlinTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

public fun Project.configureKotlin(
    plugin: KotlinBasePluginWrapper,
    javaVersion: Provider<JavaVersion>,
    compilerArgs: Provider<Set<String>>
) {
    if (plugin is KotlinMultiplatformPluginWrapper) {
        afterEvaluate {
            extensions.getByType<KotlinMultiplatformExtension>().apply {
                targets.withType<KotlinJvmTarget>().configureEach {
                    compilations.configureEach {
                        compileJavaTaskProvider?.configure {
                            targetCompatibility = javaVersion.get().toString()
                            sourceCompatibility = javaVersion.get().toString()
                        }
                        kotlinOptions.applyKotlinOptions(javaVersion, compilerArgs)
                    }
                }

                // Configures KMP targets to allow expect/actual classes that are not stable
                // https://youtrack.jetbrains.com/issue/KT-61573
                targets.all(closureOf<KotlinTarget> {
                    compilations.configureEach {
                        compilerOptions.options.freeCompilerArgs.add("-Xexpect-actual-classes")
                    }
                })
            }

            // Disable source jar tasks added by KMP
            tasks.withType<Jar>().configureEach {
                if (name == "jvmSourcesJar") {
                    eachFile {
                        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    }
                }
            }
        }
    } else {
        tasks.withType<KotlinJvmCompile>().configureEach {
            kotlinOptions.applyKotlinOptions(javaVersion, compilerArgs)
        }
    }
}

public fun Project.configureJava(javaVersion: Provider<JavaVersion>) {
    afterEvaluate {
        extensions.getByType<JavaPluginExtension>().apply {
            sourceCompatibility = javaVersion.get()
            targetCompatibility = javaVersion.get()
        }
    }
}

public fun KotlinJvmOptions.applyKotlinOptions(
    javaVersion: Provider<JavaVersion>,
    compilerArgs: Provider<Set<String>>
) {
    jvmTarget = javaVersion.get().toString()
    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    freeCompilerArgs += compilerArgs.get()
}

public fun Project.configureKotlinTest(testOptions: KotlinTestOptions) {
    tasks.withType<Test>().configureEach {
        setTestOptions(testOptions)
    }
}

public fun Test.setTestOptions(options: KotlinTestOptions) {
    val configure = (options as DefaultKotlinTestOptions).configuration.get()
    if (options.useJunitPlatform.get()) {
        useJUnitPlatform(configure)
        testLogging { events("passed", "skipped", "failed") }
    } else {
        useJUnit(configure)
    }
}
