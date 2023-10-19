# Kradle

[![][Maven Central img]][Maven Central]
[![][license img]][license]

A portmanteau of the words Kotlin and Gradle, this is a group of Gradle plugins that aim to reduce boilerplate for
regular JVM and Android modules.

---

## Plugins

Most plugins accept configuration through their respective extension blocks.

They can also be configured from the root project by applying the `*-config` plugins.
For example, to configure the target and minimum SDK for all Android modules:

```kotlin
plugins {
    id("io.github.lyxnx.gradle.android-config") version "<version>"
}

kradle {
    android {
        minSdk.set(21)
        targetSdk.set(34)
    }
}
```

Then simply apply the respective plugin depending on whether the module is a library or application

## Kotlin

Provides common configuration for Kotlin modules.

- Applies the `kotlin` plugin
- Configures the JVM target for the `org.jetbrains.kotlin.gradle.tasks.KotlinCompile` task
- Configures test options (JUnit 4/Jupiter (JUnit 5))

## Android

Both will:

- Apply the `org.gradle.android.cache-fix` plugin (if added to the root build script)
- Apply the [kotlin](#kotlin) plugin
- Configure test options from the aforementioned kotlin plugin
- Set the minimum and target SDKs
- Disable the `aidl`, `renderScript`, and `shaders`
  [build features](https://developer.android.com/reference/tools/gradle-api/8.1/com/android/build/api/dsl/BuildFeatures)

### Library

- Apply the AGP `com.android.library` plugin
- Disable the `buildConfig`, `resValues`, and `androidResources` build features

### Application

- Apply the AGP `com.android.application` plugin
- Configures the `debug` and `release` build types

## Publishing

This plugin only applies the `com.vanniktech.maven.publish` plugin and doesn't provide any other configuration.

It does, however, provide some useful functions for configuring the publishing plugin:

For example, to configure the POM as an open source GitHub project:

```kotlin
mavenPublishing {
    pom {
        githubProject("Lyxnx/kradle")
        developers {
            githubDeveloper(username = "Lyxnx", name = "Lyxnx", email = "test@test.com")
        }
        licenses {
            mit()
        }
    }
}
```

- `githubProject()` will configure the URL, issue management and scm fields
- `githubDeveloper()` will add a developer using their GitHub profile
- `mit()` will set the licence to the MIT licence

See [Licenses.kt](plugin-publish/src/main/kotlin/io/github/lyxnx/gradle/publishing/dsl/Licenses.kt)
and [MavenPom.kt](plugin-publish/src/main/kotlin/io/github/lyxnx/gradle/publishing/dsl/MavenPom.kt) for more functions

[license]:LICENCE

[license img]:https://img.shields.io/badge/license-MIT-green.svg?style=flat-square

[Maven Central]:https://maven-badges.herokuapp.com/maven-central/io.github.lyxnx.gradle/plugin-common

[Maven Central img]:https://maven-badges.herokuapp.com/maven-central/io.github.lyxnx.gradle/plugin-common/badge.svg?style=flat-square&color=blue