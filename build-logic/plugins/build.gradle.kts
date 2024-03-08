plugins {
    `kotlin-dsl`
}

dependencies {
    api(shared.kotlin.gradleplugin)
    api(shared.vanniktech.publish.gradleplugin)
    api(libs.kradle.kotlin)
}

kotlin {
    jvmToolchain(17)
}
