package io.github.lyxnx.gradle.dsl

import org.gradle.api.provider.Property

/**
 * Sets this property's value and finalizes it so no more changes can be made
 */
public fun <T : Any> Property<T>.setFinalValue(value: T) {
    set(value)
    finalizeValue()
}
