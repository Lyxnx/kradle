plugins {
    `kotlin-dsl` apply false
    alias(shared.plugins.vanniktech.publish) apply false
    alias(libs.plugins.kradle.kotlin.config)
}

kradle {
    jvmTarget = JavaVersion.VERSION_17
}
