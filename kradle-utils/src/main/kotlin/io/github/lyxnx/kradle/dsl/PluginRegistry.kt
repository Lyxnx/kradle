package io.github.lyxnx.kradle.dsl

import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.plugin.use.internal.DefaultPluginId

/**
 * Checks whether the root plugin registry has the plugin with [id]
 */
public fun PluginRegistry.hasPlugin(id: String): Boolean = lookup(DefaultPluginId.unvalidated(id)) != null
