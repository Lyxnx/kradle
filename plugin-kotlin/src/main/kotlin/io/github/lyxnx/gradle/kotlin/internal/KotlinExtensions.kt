package io.github.lyxnx.gradle.kotlin.internal

import io.github.lyxnx.gradle.KradlePlugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) {
    extensions.configure("java", configure)
}

internal fun Project.kotlin(configure: KotlinProjectExtension.() -> Unit) {
    extensions.configure("kotlin", configure)
}

internal val KradlePlugin.kotlin: KotlinProjectExtension
    get() = project.kotlin

internal val Project.kotlin: KotlinProjectExtension
    get() = extensions.getByName<KotlinProjectExtension>("kotlin")
