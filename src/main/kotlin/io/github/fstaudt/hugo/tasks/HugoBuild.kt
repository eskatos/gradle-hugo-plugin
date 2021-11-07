package io.github.fstaudt.hugo.tasks

import io.github.fstaudt.hugo.HugoPluginExtension
import io.github.fstaudt.hugo.HugoPluginExtension.Companion.SOURCE_DIRECTORY
import io.github.fstaudt.hugo.tasks.HugoDownload.Companion.BINARY_DIRECTORY
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class HugoBuild : DefaultTask() {

    companion object {
        const val HUGO_BUILD = "hugoBuild"
        const val PUBLISH_DIRECTORY = "hugo/publish"
    }

    @Nested
    lateinit var extension: HugoPluginExtension

    @Input
    var arguments: List<String> = emptyList()

    @InputDirectory
    @PathSensitive(RELATIVE)
    @IgnoreEmptyDirectories
    val baseDirectory: File = File("${project.projectDir}/$SOURCE_DIRECTORY")

    @OutputDirectory
    var outputDirectory: File = File("${project.buildDir}/$PUBLISH_DIRECTORY")

    @Input
    var publicationPath: String = ""

    @TaskAction
    fun build() {
        outputDirectory.deleteRecursively()
        project.exec {
            workingDir = baseDirectory
            executable = "${project.buildDir}/$BINARY_DIRECTORY/hugo"
            args = listOf("-d", "${outputDirectory.absolutePath}/$publicationPath") + arguments
        }
    }
}
