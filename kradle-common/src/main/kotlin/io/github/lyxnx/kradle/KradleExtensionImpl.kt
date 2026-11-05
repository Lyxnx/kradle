package io.github.lyxnx.kradle

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.findByName
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionContainer

internal abstract class KradleExtensionImpl : KradleExtension, ExtensionDefaults<KradleExtensionImpl> {

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_11)
            .finalizeValueOnRead()
        kotlinCompilerArgs
            .convention(emptySet())
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: KradleExtensionImpl) {
        jvmTarget.convention(defaults.jvmTarget)
        /*
        Don't use convention here as adding further down the line causes the convention value to be replaced

        https://github.com/gradle/gradle/issues/18352#issuecomment-925080375
         */
        kotlinCompilerArgs.set(defaults.kotlinCompilerArgs)
    }
}

internal val ExtensionContainer.kradle: KradleExtensionImpl? get() = findByName<KradleExtensionImpl>(KradleExtension.NAME)
