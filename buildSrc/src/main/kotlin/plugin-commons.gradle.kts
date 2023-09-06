import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
    id("kotlin")
    // TODO apply publishing plugin
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

// TODO use kotlin config plugin once 1st version is published to configure this sort of stuff
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType(KotlinCompile::class).configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}