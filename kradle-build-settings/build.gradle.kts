plugins {
    id("plugin-commons")
}

description = "Plugin to aid settings file configuration"

gradlePlugin {
    plugins {
        register("build-settings") {
            id = "io.github.lyxnx.kradle.build-settings"
            implementationClass = "io.github.lyxnx.kradle.settings.BuildSettingsPlugin"
            displayName = "Build settings plugin"
            description = "Configures build settings for the project"
        }
    }
}
