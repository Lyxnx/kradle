package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.LibraryExtension
import io.github.lyxnx.kradle.android.dsl.android
import org.gradle.api.Project
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(Constants.LIBRARY_PLUGIN_ID)

        configureAndroidLibrary(configPlugin.androidOptions)

        if (plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            configureKMPAndroidLibrary()
        }
    }
}

public fun Project.configureAndroidLibrary(options: AndroidOptions) {
    android<LibraryExtension> {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }

        buildFeatures {
            buildConfig = false
            resValues = false
            androidResources = false
        }

        lint {
            targetSdk = options.targetSdk.get()
        }

        testOptions {
            targetSdk = options.targetSdk.get()
        }
    }
}

public fun Project.configureKMPAndroidLibrary() {
    android<LibraryExtension> {
        sourceSets.findByName("main")?.manifest?.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets.findByName("androidTest")?.manifest?.srcFile("src/androidInstrumentedTest/AndroidManifest.xml")
    }
}
