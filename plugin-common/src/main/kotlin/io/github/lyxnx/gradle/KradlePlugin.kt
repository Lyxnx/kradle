package io.github.lyxnx.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

public abstract class KradlePlugin : Plugin<Project> {

    final override fun apply(target: Project) {

    }

    public abstract fun Project.configure()
}