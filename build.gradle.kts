plugins {
    `kotlin-dsl` apply false
    id(libs.plugins.kradle.kotlin.config.get().pluginId)
}

kradle {
    jvmTarget.set(JavaVersion.VERSION_17)
}

subprojects {
    apply(plugin = "org.gradle.kotlin.kotlin-dsl")
}
