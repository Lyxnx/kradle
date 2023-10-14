plugins {
    id("plugin-commons")
}

dependencies {
    api(projects.pluginCommon)

    implementation(common.vanniktech.publish.plugin)
}

description = "Plugins to reduce boilerplate in modules that publish artifacts"

gradlePlugin {
    plugins {
        register("publishing") {
            id = "io.github.lyxnx.gradle.publishing"
            implementationClass = "io.github.lyxnx.gradle.publishing.PublishingPlugin"
            displayName = "Maven publishing plugin"
            description = "Reduces boilerplate for a publishable module"
        }
    }
}
