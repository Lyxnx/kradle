package io.github.lyxnx.kradle.settings

import org.gradle.api.Action
import org.gradle.api.initialization.Settings

/**
 * Starts the included project configuration, setting the project's root name to [rootName]
 *
 * Example:
 * ```kotlin
 * projects("my-project") {
 *     module("module1")
 *
 *     folder("subprojects", "my-project-sub") {
 *         module("submodule1")
 *     }
 *
 *     folder("more-subprojects") {
 *         module("submodule2")
 *     }
 * }
 * ```
 *
 * is the same as
 *
 * ```kotlin
 * rootProject.name = "my-project"
 *
 * include(":module1")
 * include(":my-project-sub-submodule1")
 * project(":my-project-sub-submodule1").projectDir = file("subprojects/submodule1")
 * include(":submodule2")
 * project(":submodule2").projectDir = file("more-subprojects/submodule2")
 * ```
 * This allows you to separate modules into grouping directories with a custom prefix
 */
public fun Settings.projects(rootName: String, block: Action<ProjectsScope>) {
    rootProject.name = rootName
    block.execute(ProjectsScope(settings, emptyList(), emptyList()))
}

public class ProjectsScope internal constructor(
    private val settings: Settings,
    private val pathParts: List<String>,
    private val prefixParts: List<String>
) {

    /**
     * Defines a module, [name], within this current scope
     */
    public fun module(name: String) {
        val moduleName = (prefixParts + name).joinToString("-")
        val modulePath = (pathParts + name).joinToString("/")

        settings.include(moduleName)
        settings.project(":$moduleName").projectDir = settings.rootDir.resolve(modulePath)
    }

    /**
     * Defines a module, [name], within this current scope but also creates it as a grouping subdirectory
     *
     * For example,
     * ```kotlin
     * module("test", "test-prefix") {
     *     module("inside-test")
     * }
     * ```
     * will create a module called `test`, located in a directory with the same name, with another module called
     * `test-prefix-inside-test` located at `test/inside-test`
     *
     * This is also the same as calling
     * ```kotlin
     * include(":test")
     * include(":test-prefix-inside-test")
     * project(":test-prefix-inside-test").projectDir = file("test/inside-test")
     * ```
     *
     * By default, no prefix is applied
     */
    public fun module(name: String, prefix: String? = null, nested: Action<ProjectsScope>? = null) {
        module(name)
        folder(name, prefix, nested ?: Action {})
    }

    /**
     * Creates a subgroup folder/directory that isn't a module by itself but acts as a group for other modules
     *
     * [prefix] will be appended to any included modules within the configuration [block]
     */
    public fun folder(name: String, prefix: String? = null, block: Action<ProjectsScope>) {
        val prefixParts = if (prefix == null) prefixParts else prefixParts + prefix
        block.execute(ProjectsScope(settings, pathParts + name, prefixParts))
    }

    /**
     * Creates a subgroup directory that isn't a module by itself but acts as a group for other modules
     *
     * This is an alias for [folder]
     *
     * @see folder
     */
    public fun directory(name: String, prefix: String? = null, block: Action<ProjectsScope>) {
        folder(name, prefix, block)
    }
}
