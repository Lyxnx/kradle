package io.github.lyxnx.kradle.android.dsl

public enum class BuildType {
    /**
     * The debug build type
     */
    DEBUG,

    /**
     * The release build type
     */
    RELEASE;

    override fun toString(): String = name.lowercase()
}
