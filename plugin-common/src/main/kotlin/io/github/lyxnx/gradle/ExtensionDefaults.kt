package io.github.lyxnx.gradle

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import kotlin.reflect.KClass

/**
 * All extensions in Kradle namespace should implement this to make it possible to inherit default values from the root
 * extension
 */
public fun interface ExtensionDefaults<T> {

    /**
     * Sets the defaults for this instance
     */
    public fun setDefaults(defaults: T)

}

@PublishedApi
internal inline fun <reified T : ExtensionDefaults<T>> ExtensionContainer.createWithDefaults(
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