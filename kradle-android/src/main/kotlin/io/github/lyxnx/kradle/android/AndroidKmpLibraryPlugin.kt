package io.github.lyxnx.kradle.android

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import io.github.lyxnx.kradle.KradlePlugin
import io.github.lyxnx.kradle.android.dsl.androidComponents
import io.github.lyxnx.kradle.dsl.getOrElse
import io.github.lyxnx.kradle.dsl.ifPresent
import io.github.lyxnx.kradle.dsl.isRoot
import io.github.lyxnx.kradle.kotlin.dsl.configureKotlin
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

// TODO: refactor this along with the other android plugins
public class AndroidKmpLibraryPlugin : KradlePlugin() {

    override fun configure(project: Project) {
        if (project.isRoot && project.rootProject.subprojects.isNotEmpty()) {
            throw GradleException("Android plugins can only be applied to subprojects, not the root project")
        }

        if (!project.plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)) {
            throw GradleException("Kotlin Multiplatform Plugin must be applied to use Kradle Android Multiplatform Library plugin")
        }

        val configPlugin = project.plugins.apply(AndroidConfigPlugin::class)

        project.apply(plugin = Constants.KMP_LIBRARY_PLUGIN_ID)

        project.configureKotlin(kradleExtension.jvmTarget, kradleExtension.kotlinCompilerArgs)

        project.extensions.getByType<KotlinMultiplatformExtension>().extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
            compilerOptions {
                jvmTarget.set(kradleExtension.jvmTarget.map { JvmTarget.fromTarget(it.toString()) })
            }
        }

        project.androidComponents<KotlinMultiplatformAndroidComponentsExtension> {
            finalizeDsl {
                configPlugin.run {
                    it.configureBaseAndroidOptions(
                        options = androidOptions
                    )
                }
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun KotlinMultiplatformAndroidLibraryExtension.configureBaseAndroidOptions(options: AndroidOptions) {
        options.buildToolsVersion.ifPresent { buildToolsVersion = it }
        setCompileSdk(options.compileSdk.getOrElse { options.targetSdk.get().toString() })
        minSdk = options.minSdk.get()
        withDeviceTest {
            instrumentationRunner = Constants.ANDROIDX_TEST_RUNNER
            execution = "HOST"
        }
        lint {
            targetSdk = options.targetSdk.get()
        }
        optimization {
            consumerKeepRules.publish = true
            consumerKeepRules.files.add(project.file("consumer-rules.pro"))
        }
    }

    private fun KotlinMultiplatformAndroidLibraryExtension.setCompileSdk(version: String) {
        val intVersion = version.toIntOrNull()
        if (intVersion != null) {
            compileSdk = intVersion
        } else {
            compileSdkPreview = version
        }
    }
}
