import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties

plugins {
    id("kotlin-commons")
    id("com.vanniktech.maven.publish")
}

group = "io.github.lyxnx.gradle"
version = getRootProperties()["kradle.version"].toString()
description = "Various Kotlin gradle plugins for regular JVM and Android"

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.Companion.S01, true)
    signAllPublications()

    pom {
        name.set("Kradle")
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Lyxnx/kradle")
        licenses {
            license {
                name.set("MIT License")
                url.set("http://www.opensource.org/licenses/mit-license.php")
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
    return file("../gradle.properties").bufferedReader().use {
        Properties().apply { load(it) }
    }
}
