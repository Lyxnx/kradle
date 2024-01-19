# Changelog

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [1.0.10](#1010)
  - [Android](#android)
  - [Kotlin](#kotlin)
- [1.0.9](#109)
  - [Kotlin](#kotlin-1)
- [1.0.8](#108)
  - [Kotlin](#kotlin-2)
- [1.0.7](#107)
  - [Kotlin](#kotlin-3)
  - [Publishing](#publishing)
- [1.0.6](#106)
  - [Android](#android-1)
- [1.0.5](#105)
  - [Publishing](#publishing-1)
- [1.0.4](#104)
- [1.0.3](#103)
- [1.0.2](#102)
- [1.0.1](#101)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

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
