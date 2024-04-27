package io.github.lyxnx.gradle.android

import com.android.build.api.dsl.LibraryExtension
import io.github.lyxnx.gradle.android.internal.android
import io.github.lyxnx.gradle.internal.ANDROID_LIBRARY_PLUGIN_ID
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(ANDROID_LIBRARY_PLUGIN_ID)

        configureLibrary(configPlugin.androidOptions, kotlinPlugin)
    }

    private fun Project.configureLibrary(options: AndroidOptions, plugin: KotlinBasePluginWrapper) = android<LibraryExtension> {
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

        if (plugin is KotlinMultiplatformPluginWrapper) {
            sourceSets.findByName("main")?.manifest?.srcFile("src/androidMain/AndroidManifest.xml")
            sourceSets.findByName("androidTest")?.manifest?.srcFile("src/androidInstrumentedTest/AndroidManifest.xml")
        }
    }
}
