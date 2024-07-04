package io.github.lyxnx.kradle.dsl

import org.gradle.api.Project

/**
 * Lists the parent projects all the way up to the root project for this project instance
 */
public val Project.parents: Sequence<Project>
    get() = sequence {
        var project = project.parent
        while (project != null) {
            yield(project)
            project = project.parent
        }
    }

/**
 * Finds the gradle property [name] and returns it as a string
 *
 * If not found, this will return null rather than throwing an error
 */
public fun Project.findStringProperty(name: String): String? = providers.gradleProperty(name).orNull

/**
 * Finds the gradle property [name] and returns it as a boolean
 *
 * If not found, this will return null rather than throwing an error
 */
public fun Project.findBooleanProperty(name: String): Boolean? = findStringProperty(name)?.toBoolean()

/**
 * Finds the gradle property [name] and returns it as an integer
 *
 * If not found, this will return null rather than throwing an error
 */
public fun Project.findIntProperty(name: String): Int? = findStringProperty(name)?.toInt()

/**
 * Returns whether this project's version is a snapshot version (ends with `-SNAPSHOT`)
 */
public val Project.isSnapshotVersion: Boolean get() = version.toString().endsWith("-SNAPSHOT")

/**
 * Returns whether this project is the root project
 */
public val Project.isRoot: Boolean get() = this == rootProject
