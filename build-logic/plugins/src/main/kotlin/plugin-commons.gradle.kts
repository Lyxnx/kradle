import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties

plugins {
    id("kotlin-commons")
    id("com.vanniktech.maven.publish")
}

group = "io.github.lyxnx.gradle"
version = getRootProperties()["kradle.version"].toString()

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.Companion.S01, true)
    if (providers.gradleProperty("kradle.sign-publications").getOrElse("false").toBoolean()) {
        signAllPublications()
    }

    pom {
        name.set("Kradle")
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Lyxnx/kradle")
        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://opensource.org/license/apache-2-0")
            }
        }
        developers {
            developer {
                id.set("Lyxnx")
                name.set("Lyxnx")
                url.set("https://github.com/Lyxnx")
            }
        }
        scm {
            url.set("https://github.com/Lyxnx/kradle")
            connection.set("https://github.com/Lyxnx/kradle.git")
            developerConnection.set("scm:git:ssh://git@github.com/Lyxnx/kradle.git")
        }
    }
}

fun getRootProperties(): Properties {
    return rootProject.layout.projectDirectory.file("gradle.properties").asFile.bufferedReader().use {
        Properties().apply { load(it) }
    }
}
