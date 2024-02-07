@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        val catalogsVersion = providers.gradleProperty("catalogs.version").get()
        create("common") {
            from("io.github.lyxnx.gradle:versions-common:$catalogsVersion")
        }
    }
}

rootProject.name = "kradle"

include(":plugin-common")
include(":plugin-kotlin")
include(":plugin-android")
