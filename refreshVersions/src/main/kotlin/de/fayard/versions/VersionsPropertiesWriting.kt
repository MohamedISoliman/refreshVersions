package de.fayard.versions

import de.fayard.versions.extensions.moduleIdentifier
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

internal fun Project.updateVersionsProperties(dependenciesWithLastVersion: List<Pair<Dependency, String?>>) {
    val file = file("versions.properties")
    if (file.exists().not()) file.createNewFile()

    val properties: Map<String, String> = getVersionProperties(includeProjectProperties = false)

    val newFileContent = buildString {
        appendln(fileHeader)
        appendln()
        //TODO: Keep comments from user (ours begin with ##, while user's begin with a single #),
        // we need to find a solution to keep the order/position.
        val versionsWithUpdatesIfAvailable: List<VersionWithUpdateIfAvailable> = dependenciesWithLastVersion
            .mapNotNull { (dependency, lastVersionOrNull) ->
                dependency.moduleIdentifier?.getVersionPropertyName()?.let {
                    val currentVersion = properties[it]?.takeUnless { version -> version.isAVersionAlias()}
                        ?: return@mapNotNull null
                    val updateAvailable = currentVersion != lastVersionOrNull
                    VersionWithUpdateIfAvailable(
                        key = it,
                        currentVersion = currentVersion,
                        availableUpdateVersion = if (updateAvailable) lastVersionOrNull else null
                    )
                }
            }
            .distinctBy { it.key }
        val versionAliases: List<VersionWithUpdateIfAvailable> = properties.mapNotNull { (k, v) ->
            v.takeIf { version -> version.isAVersionAlias() }?.let {
                VersionWithUpdateIfAvailable(
                    key = k,
                    currentVersion = v,
                    availableUpdateVersion = null
                )
            }
        }
        (versionsWithUpdatesIfAvailable + versionAliases)
            .sortedBy { it.key }
            .forEach {
                val paddedKey = it.key.padStart(available.length + 2)
                val currentVersionLine = "${paddedKey}=${it.currentVersion}"
                appendln(currentVersionLine)
                it.availableUpdateVersion?.let { newVersion ->
                    append("##"); append(available.padStart(it.key.length - 2))
                    append('='); appendln(newVersion)
                }
            }
    }
    file.writeText(newFileContent)
}

private const val available = "# available"
private val fileHeader = """
    |## Dependencies and Plugin versions with their available updates
    |## Generated by ${'$'} ./gradlew refreshVersions
    |## Please, don't put extra comments in that file yet, keeping them is not supported yet.
""".trimMargin()

private class VersionWithUpdateIfAvailable(
    val key: String,
    val currentVersion: String,
    val availableUpdateVersion: String?
)
