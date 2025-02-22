package io.github.lyxnx.kradle

import io.github.lyxnx.kradle.dsl.ExtensionDefaults
import io.github.lyxnx.kradle.dsl.findByName
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty

/**
 * Extension used as a namespace for all Kradle plugins' extensions
 *
 * Eg all plugins with custom extensions will be scoped to this:
 * ```
 * kradle {
 *     otherPlugin {
 *
 *     }
 * }
 * ```
 */
public abstract class KradleExtension(project: Project) : ExtensionAware, ExtensionDefaults<KradleExtension> {

    /**
     * JVM version to apply to all modules
     */
    public val jvmTarget: Property<JavaVersion> = project.objects.property()

    /**
     * Extra compiler arguments to apply to all modules
     */
    public val kotlinCompilerArgs: SetProperty<String> = project.objects.setProperty()

    init {
        jvmTarget
            .convention(JavaVersion.VERSION_11)
            .finalizeValueOnRead()
        kotlinCompilerArgs
            .convention(emptySet())
            .finalizeValueOnRead()
    }

    override fun setDefaults(defaults: KradleExtension) {
        jvmTarget.convention(defaults.jvmTarget)
        /*
        Don't use convention here as adding further down the line causes the convention value to be replaced

        https://github.com/gradle/gradle/issues/18352#issuecomment-925080375
         */
        kotlinCompilerArgs.set(defaults.kotlinCompilerArgs)
    }

    public companion object {
        public const val NAME: String = "kradle"
    }

}

internal val ExtensionContainer.kradle: KradleExtension? get() = findByName<KradleExtension>(KradleExtension.NAME)
