@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("io.github.lyxnx.gradle.catalogs") version "2025.02.14"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "kradle"

includeBuild("build-plugins")

include(":kradle-android")
include(":kradle-common")
include(":kradle-kotlin")
include(":kradle-utils")
include(":kradle-build-settings")
