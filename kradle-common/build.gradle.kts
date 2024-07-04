plugins {
    id("plugin-commons")
}

description = "Common classes for Kradle plugins"

dependencies {
    api(projects.kradleUtils)
}
