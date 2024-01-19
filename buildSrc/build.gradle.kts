plugins {
    `kotlin-dsl`
}

dependencies {
    api(common.kotlin.gradleplugin)
    api(common.vanniktech.publish.plugin)
    api(libs.kradle.kotlin)
}

kotlin {
    jvmToolchain(17)
}
