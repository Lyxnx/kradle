plugins {
    id("plugin-commons")
}

dependencies {
    api(projects.pluginCommon)
    compileOnlyApi(common.kotlin.gradleplugin)
}

gradlePlugin {
    plugins {
        register("kotlin-config") {
            id = "io.github.lyxnx.gradle.kotlin-config"
            implementationClass = "io.github.lyxnx.gradle.kotlin.KotlinConfigPlugin"
            displayName = "Kotlin configuration plugin"
            description = "Configures Kotlin for the project"
        }
        register("kotlin-library") {
            id = "io.github.lyxnx.gradle.kotlin-library"
            implementationClass = "io.github.lyxnx.gradle.kotlin.KotlinLibraryPlugin"
            displayName = "Kotlin library plugin"
            description = "Applies defaults for a kotlin module"
        }
    }
}
