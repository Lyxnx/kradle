@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        mavenLocal()
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
