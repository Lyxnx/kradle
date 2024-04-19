package io.github.lyxnx.gradle.kotlin.dsl

import io.github.lyxnx.gradle.kotlin.DefaultKotlinTestOptions
import io.github.lyxnx.gradle.kotlin.KotlinTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.configureKotlin(
    javaVersion: Provider<JavaVersion>,
    compilerArgs: Provider<Set<String>>
) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = javaVersion.get().toString()
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            freeCompilerArgs += compilerArgs.get()
        }
    }

    dependencies {
        add("testImplementation", kotlin("test"))
    }
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
