import java.util.Properties

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(shared.kotlin.gradleplugin)
    implementation(shared.vanniktech.publish.gradleplugin)
}

kotlin {
    jvmToolchain(rootProperties["kradle.jvmTarget"].toString().toInt())
}

val rootProperties get() = Properties().apply {
    rootProject.layout.projectDirectory.file("../gradle.properties").asFile.bufferedReader().use { load(it) }
}
