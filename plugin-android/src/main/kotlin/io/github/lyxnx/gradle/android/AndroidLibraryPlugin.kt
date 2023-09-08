package io.github.lyxnx.gradle.android

import com.android.build.api.dsl.LibraryExtension
import io.github.lyxnx.gradle.android.internal.android
import org.gradle.api.Project

private const val ANDROID_LIBRARY_PLUGIN = "com.android.library"

public class AndroidLibraryPlugin : BaseAndroidPlugin() {

    override fun Project.configure() {
        applyBasePlugin(ANDROID_LIBRARY_PLUGIN)

        android<LibraryExtension> {
            buildFeatures {
                buildConfig = false
                resValues = false
                androidResources = false
            }
        }
    }
}
