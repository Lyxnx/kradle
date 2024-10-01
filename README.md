# Kradle

[![][Maven Central img]][Maven Central]
[![][license img]][license]

A portmanteau of the words Kotlin and Gradle, this is a group of Gradle plugins that aim to reduce boilerplate for
regular JVM and Android modules.

---

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Plugin Mapping](#plugin-mapping)
- [Kotlin](#kotlin)
- [Android](#android)
  - [Library](#library)
  - [Application](#application)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

---

Most plugins accept configuration through their respective extension blocks.

They can also be configured from the root project by applying the `*-config` plugins.
For example, to configure the target and minimum SDK for all Android modules:

```kotlin
plugins {
    id("io.github.lyxnx.kradle.android-config") version "<version>"
}

kradle {
    android {
        minSdk = 21
        targetSdk = 34
    }
}
```

Then simply apply the respective plugin depending on whether the module is a library or application

Most plugins will also adjust their configuration if the Kotlin Multiplatform plugin is applied

## Plugin Mapping

| ID                                           | Class                                                     | Description                                                               |
|----------------------------------------------|-----------------------------------------------------------|---------------------------------------------------------------------------|
| `io.github.lyxnx.kradle.kotlin-config`       | `io.github.lyxnx.kradle.kotlin.KotlinConfigPlugin`        | Allows configuring of Kotlin properties for the project                   |
| `io.github.lyxnx.kradle.kotlin-library`      | `io.github.lyxnx.kradle.kotlin.KotlinLibraryPlugin`       | Applies defaults for a kotlin module                                      |
| `io.github.lyxnx.kradle.android-config`      | `io.github.lyxnx.kradle.android.AndroidConfigPlugin`      | Allows configuration of Android properties for applications and libraries |
| `io.github.lyxnx.kradle.android-application` | `io.github.lyxnx.kradle.android.AndroidApplicationPlugin` | Configures Android application modules                                    |
| `io.github.lyxnx.kradle.android-library`     | `io.github.lyxnx.kradle.android.AndroidLibraryPlugin`     | Configures Android library modules                                        |

## Kotlin

Provides common configuration for Kotlin modules.

- Applies the `kotlin` plugin
- Configures the JVM versions
- Configures test options (JUnit 4/Jupiter (JUnit 5))

## Android

Both will:

- Apply the `org.gradle.android.cache-fix` plugin (if added)
    - If not applied, a warning will be shown that can be suppressed with `kradle.android.suppressCacheFixWarning=true`
      in your `gradle.properties`
- Apply the [kotlin](#kotlin) plugin
- Configure test options from the aforementioned kotlin plugin
- Set the minimum and target SDKs
- Disable the `aidl`, `renderScript`, and `shaders`
  [build features](https://developer.android.com/reference/tools/gradle-api/8.5/com/android/build/api/dsl/BuildFeatures)

### Library

- Apply the AGP `com.android.library` plugin
- Disable the `buildConfig`, `resValues`, and `androidResources` build features

### Application

- Apply the AGP `com.android.application` plugin
- Configures the `debug` and `release` build types

[license]:LICENCE

[license img]:https://img.shields.io/badge/License-Apache_2.0-green.svg?style=flat-square

[Maven Central]:https://maven-badges.herokuapp.com/maven-central/io.github.lyxnx.kradle/kradle-common

[Maven Central img]:https://maven-badges.herokuapp.com/maven-central/io.github.lyxnx.kradle/kradle-common/badge.svg?style=flat-square&color=blue
