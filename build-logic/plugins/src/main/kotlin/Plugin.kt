import org.gradle.plugin.devel.PluginDeclaration

fun PluginDeclaration.tags(vararg tags: String) {
    this.tags.set(listOf("kradle") + tags)
}
