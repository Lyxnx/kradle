package io.github.lyxnx.gradle

import io.github.lyxnx.gradle.dsl.findByName
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer

@Suppress("LeakingThis")
internal abstract class KradleExtensionImpl : KradleExtension, ExtensionAware, ExtensionDefaults<KradleExtensionImpl> {

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_11)
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: KradleExtensionImpl) {
        jvmTarget.convention(defaults.jvmTarget)
    }

}

@PublishedApi
internal val ExtensionContainer.kradle: KradleExtensionImpl?
    get() = findByName<KradleExtensionImpl>(KradleExtension.NAME)