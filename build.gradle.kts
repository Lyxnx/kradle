plugins {
    `kotlin-dsl` apply false

    alias(libs.plugins.kotlinx.binary.compatibility.validator)
}

subprojects {
    apply(plugin = "org.gradle.kotlin.kotlin-dsl")
}
