package io.github.lyxnx.kradle.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

// Not actually needed but allows consumer to pull in project scope stuff
public class BuildSettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
    }
}
