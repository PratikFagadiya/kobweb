@file:Suppress("LeakingThis") // Following official Gradle guidance

package com.varabyte.kobweb.gradle.library.tasks

import com.varabyte.kobweb.gradle.core.KOBWEB_METADATA_BACKEND
import com.varabyte.kobweb.gradle.core.extensions.KobwebBlock
import com.varabyte.kobweb.gradle.core.processors.BackendDataProcessor
import com.varabyte.kobweb.gradle.core.project.backend.BackendData
import com.varabyte.kobweb.gradle.core.project.common.PackageUtils.resolvePackageShortcut
import com.varabyte.kobweb.gradle.core.tasks.KobwebGenerateMetadataTask
import com.varabyte.kobweb.gradle.core.utils.LoggingReporter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

abstract class KobwebGenerateMetadataBackendTask @Inject constructor(kobwebBlock: KobwebBlock)
    : KobwebGenerateMetadataTask<BackendData>(kobwebBlock, "Generate Kobweb metadata about this project's backend structure that can be consumed later by a Kobweb app.") {

    override fun getSourceFiles() = getSourceFilesJvm()
    override fun getGeneratedMetadataFile() = File(kobwebBlock.getGenJvmResRoot(project), KOBWEB_METADATA_BACKEND)

    override fun createProcessor() = BackendDataProcessor(
        LoggingReporter(project.logger),
        resolvePackageShortcut(project.group.toString(), kobwebBlock.apiPackage.get())
    )

    override fun encodeToString(value: BackendData) = Json.encodeToString(value)
}
