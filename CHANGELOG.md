# Changelog

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [2.0.4](#204)
  - [Android](#android)
- [2.0.3](#203)
  - [Android](#android-1)
  - [Common](#common)
  - [Kotlin](#kotlin)
- [2.0.2](#202)
- [2.0.1](#201)
- [2.0.0](#200)
- [1.1.2](#112)
- [1.1.1](#111)
- [1.1.0](#110)
- [1.0.15](#1015)
- [1.0.14](#1014)
- [1.0.13](#1013)
  - [Android](#android-2)
- [1.0.12](#1012)
  - [Android](#android-3)
- [1.0.11](#1011)
  - [Publishing](#publishing)
- [1.0.10](#1010)
  - [Android](#android-4)
  - [Kotlin](#kotlin-1)
- [1.0.9](#109)
  - [Kotlin](#kotlin-2)
- [1.0.8](#108)
  - [Kotlin](#kotlin-3)
- [1.0.7](#107)
  - [Kotlin](#kotlin-4)
  - [Publishing](#publishing-1)
- [1.0.6](#106)
  - [Android](#android-5)
- [1.0.5](#105)
  - [Publishing](#publishing-2)
- [1.0.4](#104)
- [1.0.3](#103)
- [1.0.2](#102)
- [1.0.1](#101)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 2.0.4

### Android 

- Supports AGP 8.11.0
- Remove `.debug` suffix from application IDs for debug build type

## 2.0.3

- Add new `io.github.lyxnx.kradle.build-settings` plugin for working with settings configuration files

### Android

- Removed android cachefix plugin warning
- Updated default target sdk to 35 from 34

### Common

- Fix kotlin compiler args being overridden in child projects

### Kotlin

- Update junit5 configuration slightly

## 2.0.2

Fix compile sdk issue with android plugin when overriding default value from root project in another subproject 

## 2.0.1

Add flattenedProjectTree utility function

## 2.0.0

Rename plugin IDs to `io.github.lyxnx.kradle.*` - this is a breaking change

Fix issue with android library lint and test option sdks being set too early in single module projects

## 1.1.2

Fix issue with kotlin extension target property not being set quickly enough

## 1.1.1

Support KMP

## 1.1.0

Stop plugins breaking when trying to reconfigure some already configured values

## 1.0.15

Fix issue with source+targetCompatibility values not being set for java task

## 1.0.14

Mostly internal changes

Plugins are now merged into a single `kradle-plugin` artifact

## 1.0.13

### Android

- Fix testOptions targetSdk applying to applications

## 1.0.12

### Android

- Support AGP 8.3.0
- Configure lint and testOptions targetSdk values 

## 1.0.11

### Publishing

- Retired publishing plugin since the plugin itself added no functionality - the extension functions are now part of the
  common module

## 1.0.10

- Update JDKs used to 17
- Relicensed as Apache 2.0
- Updated to gradle 8.5

### Android

- Add `kotlin("test")` library to `androidTestImplementation` configuration
- Added default AndroidJUnitRunner test runner to both application and library projects

### Kotlin

- Add `kotlin("test")` library to `testImplementation` configuration

## 1.0.9

### Kotlin

- Fix issue with flat projects jvmTarget value being read too early

## 1.0.8

### Kotlin

- Fix erroneous warning when configuring JVM toolchain as the else block was being evaluated regardless

## 1.0.7

### Kotlin

- Configure using JVM toolchain instead of setting compatibilities manually

### Publishing

- Only require username for githubDeveloper function

## 1.0.6

- Update catalogs versions to 2023.11.03

### Android

- Add configuration to apply proguard files

## 1.0.5

- License project as MIT

### Publishing

- Fix issue when vanniktech plugin code is not available to consumers of the publish plugin

## 1.0.4

- Add publishing plugin [io.github.lyxnx.gradle.publishing](plugin-publish)

## 1.0.3

- Update catalogs versions to 2023.09.10

## 1.0.2

- Internal change

## 1.0.1

- Initial release
