plugins {
    id("kotlin-commons")
    id("com.vanniktech.maven.publish")
}

group = "io.github.lyxnx.kradle"
version = providers.gradleProperty("kradle.version").get()

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())
    publishToMavenCentral(true)

    if (!version.toString().endsWith("SNAPSHOT")) {
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
                id.set("lyxnx")
                name.set("Lyxnx")
                url.set("https://github.com/Lyxnx")
            }
        }
        scm {
            url.set("https://github.com/Lyxnx/kradle")
            connection.set("https://github.com/Lyxnx/kradle.git")
            developerConnection.set("scm:git:ssh://git@github.com:Lyxnx/kradle.git")
        }
    }
}
