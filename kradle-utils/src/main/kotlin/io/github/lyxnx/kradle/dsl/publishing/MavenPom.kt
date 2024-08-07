@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.kradle.dsl.publishing

import org.gradle.api.Action
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributor
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec

/**
 * Adds a GitHub based developer to this POM
 *
 * @param username GitHub username of developer
 * @param name name of the developer, whether their real name, or online pseudonym. By default this will be the same as
 * [username]
 * @param email email of the developer - typically the same email used for GitHub commits. Empty by default and will not
 * be included in the final POM
 * @param action any additional actions to configure this developer
 */
public fun MavenPomDeveloperSpec.githubDeveloper(
    username: String,
    name: String = username,
    email: String = "",
    action: Action<MavenPomDeveloper>? = null
) {
    developer {
        id.set(username)
        this.name.set(name)
        url.set("https://$GITHUB/$username")
        if (email.isNotBlank()) {
            this.email.set(email)
        }
        action?.execute(this)
    }
}

/**
 * Convenience function to add a developer to this POM
 *
 * @param id unique identifier for this developer - this could be a username or some other identifier
 * @param name name of this developer - typically their real name, but could be a username/pseudonym
 * @param email email of this developer - typically the same email used for commits
 * @param action any additional actions to configure this developer
 */
public fun MavenPomDeveloperSpec.developer(
    id: String,
    name: String,
    email: String,
    action: Action<MavenPomDeveloper>? = null
) {
    developer {
        this.id.set(id)
        this.name.set(name)
        this.email.set(email)
        action?.execute(this)
    }
}

/**
 * Convenience function to add a contributor to this POM
 *
 * @param name the name of the contributor, whether their real name, or online pseudonym
 * @param email email of the contributor - typically the same email used for commits/other communication
 * @param action any additional actions to configure this contributor
 */
public fun MavenPomContributorSpec.contributor(
    name: String,
    email: String,
    action: Action<MavenPomContributor>? = null,
) {
    contributor {
        this.name.set(name)
        this.email.set(email)
        action?.execute(this)
    }
}

private const val GITHUB = "github.com"

/**
 * Sets up the POM for this project to be GitHub based
 *
 * This will set the URL, issue management, and SCM information to the corresponding GitHub information for [projectName]
 *
 * For example, if the GitHub URL of a project is github.com/testcompany/testproject, then the [projectName] would be
 * `testcompany/testproject`
 */
public fun MavenPom.githubProject(projectName: String) {
    val githubUrl = "https://$GITHUB/$projectName"

    url.set(githubUrl)

    issueManagement {
        url.set("$githubUrl/issues")
        system.set("GitHub Issues")
    }

    scm {
        url.set(githubUrl)
        connection.set("scm:git:git://$GITHUB/$projectName.git")
        developerConnection.set("scm:git:ssh://$GITHUB/$projectName.git")
    }
}
