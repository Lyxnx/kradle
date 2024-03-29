package io.github.lyxnx.gradle.dsl

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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
@OptIn(ExperimentalContracts::class)
public inline fun <R, T : R> Provider<T>.getOrElse(orElse: () -> R): R {
    contract {
        callsInPlace(orElse, kotlin.contracts.InvocationKind.AT_MOST_ONCE)
    }
    return if (isPresent) get() else orElse()
}