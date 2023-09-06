@file:Suppress("UnstableApiUsage")

import java.util.Properties

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
        val catalogsVersion = getRootProperties()["catalogs.version"].toString()
        create("common") {
            from("io.github.lyxnx.gradle:versions-common:$catalogsVersion")
        }
    }
}

fun getRootProperties(): Properties {
    return file("../gradle.properties").bufferedReader().use {
        Properties().apply { load(it) }
    }
}