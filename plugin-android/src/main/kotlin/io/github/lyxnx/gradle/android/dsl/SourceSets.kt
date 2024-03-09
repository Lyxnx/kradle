package io.github.lyxnx.gradle.android.dsl

import com.android.SdkConstants.FD_AIDL
import com.android.SdkConstants.FD_ASSETS
import com.android.SdkConstants.FD_JAVA
import com.android.SdkConstants.FD_JAVA_RES
import com.android.SdkConstants.FD_ML_MODELS
import com.android.SdkConstants.FD_RENDERSCRIPT
import com.android.SdkConstants.FD_RES
import com.android.SdkConstants.FN_ANDROID_MANIFEST_XML
import com.android.build.api.dsl.AndroidSourceSet
import io.github.lyxnx.gradle.android.internal.AndroidCommonExtension
import org.gradle.kotlin.dsl.get
import java.util.Locale

/**
 * Adds a source set with a given name [name] shared between [variant1] and [variant2]
 *
 * Eg if we want to share between debug and a custom variant:
 * ```
 * android {
 *     addSharedSourceSetRoot("debug", "customvariant")
 * }
 * ```
 */
public fun AndroidCommonExtension.addSharedSourceSetRoot(
    variant1: String,
    variant2: String,
    name: String = "$variant1${variant2.capitalized()}}"
) {
    val variant1Sources = sourceSets[variant1]
    val variant2Sources = sourceSets[variant2]
    val root = "src/$name"

    variant1Sources.addRoot(root)
    variant2Sources.addRoot(root)
}

private fun AndroidSourceSet.addRoot(path: String) {
    java.srcDirs("$path/$FD_JAVA")
    kotlin.srcDirs("$path/$FD_JAVA", "$path/kotlin")
    resources.srcDir("$path/$FD_JAVA_RES")
    res.srcDir("$path/$FD_RES")
    assets.srcDir("$path/$FD_ASSETS")
    manifest.srcFile("$path/$FN_ANDROID_MANIFEST_XML")
    aidl.srcDir("$path/$FD_AIDL")
    renderscript.srcDir("$path/$FD_RENDERSCRIPT")
    jniLibs.srcDir("$path/jniLibs")
    shaders.srcDir("$path/shaders")
    mlModels.srcDir("$path/$FD_ML_MODELS")
}

private fun String.capitalized(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
