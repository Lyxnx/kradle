package io.github.lyxnx.gradle.dsl

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import kotlin.contracts.contract
import kotlin.contracts.InvocationKind

/**
 * Sets this property's value and finalizes it so no more changes can be made
 */
public fun <T : Any> Property<T>.setFinalValue(value: T) {
    set(value)
    finalizeValue()
}

/**
 * Gets the value of this provider, if one is [present][Provider.isPresent], otherwise returns the result of [orElse]
 *
 * Note that [orElse] is lazily evaluated, and only called if no value is present
 */
public inline fun <R, T : R> Provider<T>.getOrElse(orElse: () -> R): R {
    contract {
        callsInPlace(orElse, InvocationKind.AT_MOST_ONCE)
    }
    return if (isPresent) get() else orElse()
}

/**
 * Executes [action] if and only if this provider has a value and returns the original provider for chaining
 */
public inline fun <T> Provider<T>.ifPresent(action: (T) -> Unit): Provider<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }

    if (isPresent) action(get())
    return this
}
