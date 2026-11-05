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
    id("io.github.lyxnx.gradle.catalogs") version "2026.10.30"
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
            // 9.5.0 has an issue atm https://issuetracker.google.com/issues/572377862
            version("android-gradle-plugin", "9.4.1")
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
