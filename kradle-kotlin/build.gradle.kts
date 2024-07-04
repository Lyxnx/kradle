plugins {
    id("plugin-commons")
}

dependencies {
    api(projects.kradleCommon)
    compileOnlyApi(shared.kotlin.gradleplugin)
}

description = "Plugins to reduce boilerplate in Kotlin modules"

gradlePlugin {
    plugins {
        register("kotlin-config") {
            id = "io.github.lyxnx.kradle.kotlin-config"
            implementationClass = "io.github.lyxnx.kradle.kotlin.KotlinConfigPlugin"
            displayName = "Kotlin configuration plugin"
            description = "Configures Kotlin for the project"
        }
        register("kotlin-library") {
            id = "io.github.lyxnx.kradle.kotlin-library"
            implementationClass = "io.github.lyxnx.kradle.kotlin.KotlinLibraryPlugin"
            displayName = "Kotlin library plugin"
            description = "Applies defaults for a kotlin module"
        }
    }
}
