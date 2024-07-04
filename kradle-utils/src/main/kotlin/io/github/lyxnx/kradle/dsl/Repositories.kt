package io.github.lyxnx.kradle.dsl

import org.gradle.api.Action
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.maven

/**
 * Adds the JitPack repository
 */
public fun RepositoryHandler.jitpack(configure: Action<MavenArtifactRepository>? = null): MavenArtifactRepository {
    return maven("https://jitpack.io") {
        name = "jitpack"
        configure?.execute(this)
    }
}

/**
 * Adds the GitHub Packages repository for [repositoryName]
 *
 * If [username] and [password] are not specified (or null), these values will be looked up via the `githubPackagesUsername`
 * and `githubPackagesPassword` properties
 *
 * Eg:
 * ```kotlin
 * repositories {
 *    githubPackages("Lyxnx/kradle")
 *    // or
 *    githubPackages("Lyxnx/kradle", "username", "password")
 * }
 */
public fun RepositoryHandler.githubPackages(
    repositoryName: String,
    username: String? = null,
    password: String? = null,
    configure: Action<MavenArtifactRepository>? = null
): MavenArtifactRepository {
    return maven("https://maven.pkg.github.com/$repositoryName") {
        name = "githubPackages"
        credentials(PasswordCredentials::class) {
            if (username != null && password != null) {
                this.username = username
                this.password = password
            }
        }
        configure?.execute(this)
    }
}
