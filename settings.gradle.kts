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
    id("io.github.lyxnx.gradle.android-catalogs") version "2024.03.08"
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

includeBuild("build-logic")

include(":plugin-common")
include(":plugin-kotlin")
include(":plugin-android")
