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
- Configures the JVM toolchain
    - If using Android, AGP will automatically configure the `sourceCompatibility` and `targetCompatibility` based on
      this value
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

[license]:LICENCE

[license img]:https://img.shields.io/badge/License-Apache_2.0-green.svg?style=flat-square

[Maven Central]:https://maven-badges.herokuapp.com/maven-central/io.github.lyxnx.gradle/plugin-common

[Maven Central img]:https://maven-badges.herokuapp.com/maven-central/io.github.lyxnx.gradle/plugin-common/badge.svg?style=flat-square&color=blue
