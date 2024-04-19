import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("io.github.lyxnx.gradle.kotlin-library")
    id("com.vanniktech.maven.publish")
}

group = "io.github.lyxnx.gradle"
version = providers.gradleProperty("kradle.version").get()
description = "Various Gradle plugins for Kotlin/Android boilerplate"

dependencies {
    // Kotlin plugin
    compileOnly(shared.kotlin.gradleplugin)

    // Android plugin
    compileOnly(shared.android.gradleplugin)
    compileOnly(libs.androidtools.common)
}

gradlePlugin {
    plugins {
        // Kotlin plugins
        register("kotlin-config") {
            id = "io.github.lyxnx.gradle.kotlin-config"
            implementationClass = "io.github.lyxnx.gradle.kotlin.KotlinConfigPlugin"
            displayName = "Kotlin configuration plugin"
            description = "Configures Kotlin for the project"
        }
        register("kotlin-library") {
            id = "io.github.lyxnx.gradle.kotlin-library"
            implementationClass = "io.github.lyxnx.gradle.kotlin.KotlinLibraryPlugin"
            displayName = "Kotlin library plugin"
            description = "Applies defaults for a kotlin module"
        }

        // Android plugins
        register("android-config") {
            id = "io.github.lyxnx.gradle.android-config"
            implementationClass = "io.github.lyxnx.gradle.android.AndroidConfigPlugin"
            displayName = "Android configuration"
            description = "Configurations for Android application and library modules"
            tags("android")
        }
        register("android-application") {
            id = "io.github.lyxnx.gradle.android-application"
            implementationClass = "io.github.lyxnx.gradle.android.AndroidApplicationPlugin"
            displayName = "Android application"
            description = "Configures Android application modules"
            tags("android", "application")
        }
        register("android-library") {
            id = "io.github.lyxnx.gradle.android-library"
            implementationClass = "io.github.lyxnx.gradle.android.AndroidLibraryPlugin"
            displayName = "Android library"
            description = "Configures Android library modules"
            tags("android", "library")
        }
    }
}

kotlin {
    explicitApi()
}

private fun PluginDeclaration.tags(vararg tags: String) {
    this.tags.set(listOf("kradle") + tags)
}

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.Companion.S01, true)
    if (providers.gradleProperty("kradle.sign-publications").getOrElse("false").toBoolean()) {
        signAllPublications()
    }

    pom {
        name.set("Kradle")
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Lyxnx/kradle")
        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://opensource.org/license/apache-2-0")
            }
        }
        developers {
            developer {
                id.set("Lyxnx")
                name.set("Lyxnx")
                url.set("https://github.com/Lyxnx")
            }
        }
        scm {
            url.set("https://github.com/Lyxnx/kradle")
            connection.set("https://github.com/Lyxnx/kradle.git")
            developerConnection.set("scm:git:ssh://git@github.com/Lyxnx/kradle.git")
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.contracts.ExperimentalContracts")
}
