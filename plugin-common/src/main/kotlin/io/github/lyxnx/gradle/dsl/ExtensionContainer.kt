package io.github.lyxnx.gradle.dsl

import org.gradle.api.plugins.ExtensionContainer

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public inline fun <reified T : Any> ExtensionContainer.findByName(name: String): T? {
    return findByName(name)?.let {
        it as? T ?: error(
            "Element $name of type ${it::class.java.name} from container $this cannot be cast to ${T::class.qualifiedName}"
        )
    }
}