plugins {
    `kotlin-dsl` apply false
    alias(libs.plugins.kradle.kotlin.config)
}

kradle {
    jvmTarget = JavaVersion.VERSION_17
}

subprojects {
    apply(plugin = "org.gradle.kotlin.kotlin-dsl")
}
