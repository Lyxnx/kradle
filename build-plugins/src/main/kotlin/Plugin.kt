import org.gradle.plugin.devel.PluginDeclaration

@Suppress("UnstableApiUsage")
fun PluginDeclaration.tags(vararg tags: String) {
    this.tags.set(listOf("kradle") + tags)
}
