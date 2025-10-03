plugins {
    id("plugin-commons")
}

dependencies {
    api(projects.kradleKotlin)

    compileOnly(shared.android.gradleplugin.api)
    compileOnly(libs.androidtools.common)
}

description = "Plugins to reduce boilerplate in Android modules"

gradlePlugin {
    plugins {
        register("android-config") {
            id = "io.github.lyxnx.kradle.android-config"
            implementationClass = "io.github.lyxnx.kradle.android.AndroidConfigPlugin"
            displayName = "Android configuration"
            description = "Configurations for Android application and library modules"
            tags("android")
        }

        register("android-application") {
            id = "io.github.lyxnx.kradle.android-application"
            implementationClass = "io.github.lyxnx.kradle.android.AndroidApplicationPlugin"
            displayName = "Android application"
            description = "Configures Android application modules"
            tags("android", "application")
        }

        register("android-library") {
            id = "io.github.lyxnx.kradle.android-library"
            implementationClass = "io.github.lyxnx.kradle.android.AndroidLibraryPlugin"
            displayName = "Android library"
            description = "Configures Android library modules"
            tags("android", "library")
        }
    }
}
