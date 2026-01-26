@file:Suppress("UnstableApiUsage")

import io.github.lyxnx.gradle.catalogs.shared

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("io.github.lyxnx.gradle.catalogs") version "2026.01.25"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        shared {
            // Not in latest catalog yet
            version("android-gradle-plugin", "9.0.0")
        }
    }
}

rootProject.name = "kradle"

includeBuild("build-plugins")

include(":kradle-android")
include(":kradle-common")
include(":kradle-kotlin")
include(":kradle-utils")
include(":kradle-build-settings")
