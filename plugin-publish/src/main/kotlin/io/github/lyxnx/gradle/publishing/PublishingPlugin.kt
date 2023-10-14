package io.github.lyxnx.gradle.publishing

import com.vanniktech.maven.publish.MavenPublishPlugin
import io.github.lyxnx.gradle.KradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/*
This plugin doesn't do much by itself except includes and applies the vanniktech plugin but provides some useful extension functions
 */

public class PublishingPlugin : KradlePlugin() {

    override fun Project.configure() {
        plugins.apply(MavenPublishPlugin::class)
    }

}
