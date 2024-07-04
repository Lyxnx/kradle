package io.github.lyxnx.kradle.dsl

import org.gradle.api.plugins.ExtensionContainer

/**
 * Finds an extension of type [T] with [name]
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public inline fun <reified T : Any> ExtensionContainer.findByName(name: String): T? {
    return findByName(name)?.let {
        it as? T ?: error(
            "Element $name of type ${it::class.java.name} from container $this cannot be cast to ${T::class.qualifiedName}"
        )
    }
}
