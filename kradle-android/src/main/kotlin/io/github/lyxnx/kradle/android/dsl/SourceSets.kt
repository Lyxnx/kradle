package io.github.lyxnx.kradle.android.dsl

import com.android.SdkConstants.FD_AIDL
import com.android.SdkConstants.FD_ASSETS
import com.android.SdkConstants.FD_JAVA
import com.android.SdkConstants.FD_JAVA_RES
import com.android.SdkConstants.FD_ML_MODELS
import com.android.SdkConstants.FD_RENDERSCRIPT
import com.android.SdkConstants.FD_RES
import com.android.SdkConstants.FN_ANDROID_MANIFEST_XML
import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.CommonExtension
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
public fun CommonExtension.addSharedSourceSetRoot(
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
    java.directories += "$path/$FD_JAVA"
    kotlin.directories += "$path/$FD_JAVA"
    kotlin.directories += "$path/kotlin"
    resources.directories += "$path/$FD_JAVA_RES"
    res.directories += "$path/$FD_RES"
    assets.directories += "$path/$FD_ASSETS"
    manifest.srcFile("$path/$FN_ANDROID_MANIFEST_XML")
    aidl.directories += "$path/$FD_AIDL"
    renderscript.directories += "$path/$FD_RENDERSCRIPT"
    jniLibs.directories += "$path/jniLibs"
    shaders.directories += "$path/shaders"
    mlModels.directories += "$path/$FD_ML_MODELS"
}

private fun String.capitalized(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
