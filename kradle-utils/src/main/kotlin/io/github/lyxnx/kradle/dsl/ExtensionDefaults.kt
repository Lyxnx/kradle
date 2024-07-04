package io.github.lyxnx.kradle.dsl

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import kotlin.reflect.KClass

/**
 * Represents an extension that has default values
 *
 * Extensions in Kradle namespace that are configurable from the root project should implement this to make it possible
 * to inherit default values
 */
public fun interface ExtensionDefaults<T> {

    /**
     * Sets the defaults for this instance
     */
    public fun setDefaults(defaults: T)

}

/**
 * Creates an extension with [name] and applies [defaults] to it
 *
 * [publicType] is used to specify the public type of the extension if it differs from the actual type (eg a public
 * interface and an internal implementation class)
 */
public inline fun <reified T : ExtensionDefaults<T>> ExtensionContainer.createWithDefaults(
    name: String,
    defaults: T?,
    publicType: KClass<in T>? = null,
): T {
    val extension = if (publicType == null) {
        create(name)
    } else {
        create(
            publicType = publicType,
            name = name,
            instanceType = T::class
        ) as T
    }

    return extension.apply { if (defaults != null) setDefaults(defaults) }
}
