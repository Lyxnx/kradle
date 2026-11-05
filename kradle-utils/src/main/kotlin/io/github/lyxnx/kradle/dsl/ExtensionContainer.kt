package io.github.lyxnx.kradle.dsl

import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

/**
 * Finds an extension of type [T] with [name]
 */
public inline fun <reified T : Any> ExtensionContainer.findByName(name: String): T? {
    return findByName(name, T::class)
}

/**
 * Finds an extension of type [type] with [name]
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public fun <T : Any> ExtensionContainer.findByName(name: String, type: KClass<T>): T? {
    return findByName(name)?.let {
        if (type.isInstance(it)) {
            @Suppress("UNCHECKED_CAST")
            it as T
        } else {
            error(
                "Element $name of type ${it::class.java.name} from container $this cannot be cast to ${type.qualifiedName}"
            )
        }
    }
}
