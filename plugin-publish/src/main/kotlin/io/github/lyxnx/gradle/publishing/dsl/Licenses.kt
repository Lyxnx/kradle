package io.github.lyxnx.gradle.publishing.dsl

import org.gradle.api.publish.maven.MavenPomLicenseSpec

/**
 * Convenience function to add the MIT license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.mit() {
    license {
        name.set("The MIT License")
        url.set("https://opensource.org/license/mit/")
    }
}

/**
 * Convenience function to add the Apache version 2 license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.apacheV2() {
    license {
        name.set("Apache License, Version 2.0")
        url.set("https://opensource.org/license/apache-2-0/")
    }
}

/**
 * Convenience function to add the GNU GPL version 2 license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.gpl2() {
    license {
        name.set("GNU General Public License version 2")
        url.set("https://opensource.org/license/gpl-2-0/")
    }
}

/**
 * Convenience function to add the GNU GPL version 3 license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.gpl3() {
    license {
        name.set("GNU General Public License version 3")
        url.set("https://opensource.org/license/gpl-3-0/")
    }
}

/**
 * Convenience function to add the GNU LGPL version 2.1 license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.lgpl2() {
    license {
        name.set("GNU Lesser General Public License version 2.1")
        url.set("https://opensource.org/license/lgpl-2-1/")
    }
}

/**
 * Convenience function to add the GNU LGPL version 3 license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.lgpl3() {
    license {
        name.set("GNU Lesser General Public License version 3")
        url.set("https://opensource.org/license/lgpl-3-0/")
    }
}

/**
 * Convenience function to add the Mozilla Public license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.mozilla() {
    license {
        name.set("Mozilla Public License 2.0")
        url.set("https://opensource.org/license/mpl-2-0/")
    }
}

/**
 * Convenience function to add the 2 clause BSD license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.bsd2() {
    license {
        name.set("The 2-Clause BSD License")
        url.set("https://opensource.org/license/lgpl-2-1/")
    }
}

/**
 * Convenience function to add the 3 clause BSD license to the POM
 *
 * *Note that it is up to the developer to ensure that the license is correct for the use case*
 */
public fun MavenPomLicenseSpec.bsd3() {
    license {
        name.set("he 3-Clause BSD License")
        url.set("https://opensource.org/license/bsd-3-clause/")
    }
}
