package com.varabyte.kobweb.cli.export

import com.varabyte.kobweb.cli.common.Anims
import com.varabyte.kobweb.cli.common.GradleAlertBundle
import com.varabyte.kobweb.cli.common.KobwebGradle
import com.varabyte.kobweb.cli.common.consumeProcessOutput
import com.varabyte.kobweb.cli.common.findKobwebApplication
import com.varabyte.kobweb.cli.common.assertKobwebApplication
import com.varabyte.kobweb.cli.common.assertServerNotAlreadyRunning
import com.varabyte.kobweb.cli.common.handleConsoleOutput
import com.varabyte.kobweb.cli.common.handleGradleOutput
import com.varabyte.kobweb.cli.common.isServerAlreadyRunningFor
import com.varabyte.kobweb.cli.common.newline
import com.varabyte.kobweb.cli.common.showStaticSiteLayoutWarning
import com.varabyte.kobweb.server.api.SiteLayout
import com.varabyte.kobweb.server.api.ServerEnvironment
import com.varabyte.kotter.foundation.anim.textAnimOf
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.text.red
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.foundation.text.yellow
import kotlinx.coroutines.delay

private enum class ExportState {
    EXPORTING,
    FINISHING,
    FINISHED,
    CANCELLING,
    CANCELLED,
    INTERRUPTED,
}

@Suppress("BlockingMethodInNonBlockingContext")
fun handleExport(siteLayout: SiteLayout, isInteractive: Boolean) {
    val kobwebGradle = KobwebGradle(ServerEnvironment.PROD) // exporting is a production-only action

    if (isInteractive) session {
        val kobwebApplication = findKobwebApplication() ?: return@session
        if (isServerAlreadyRunningFor(kobwebApplication)) return@session

        newline() // Put space between user prompt and eventual first line of Gradle output

        if (siteLayout == SiteLayout.STATIC) {
            showStaticSiteLayoutWarning()
        }

        var exportState by liveVarOf(ExportState.EXPORTING)
        val gradleAlertBundle = GradleAlertBundle(this)

        var cancelReason by liveVarOf("")
        val ellipsis = textAnimOf(Anims.ELLIPSIS)
        var exception by liveVarOf<Exception?>(null) // Set if ExportState.INTERRUPTED
        section {
            textLine() // Add space between this block and Gradle text which will appear above
            gradleAlertBundle.renderInto(this)
            when (exportState) {
                ExportState.EXPORTING -> textLine("Exporting$ellipsis")
                ExportState.FINISHING -> textLine("Finishing up$ellipsis")
                ExportState.FINISHED -> textLine("Export finished successfully")
                ExportState.CANCELLING -> yellow { textLine("Cancelling export: $cancelReason$ellipsis") }
                ExportState.CANCELLED -> yellow { textLine("Export cancelled: $cancelReason") }
                ExportState.INTERRUPTED -> {
                    red { textLine("Interrupted by exception:") }
                    textLine()
                    textLine(exception!!.stackTraceToString())
                }
            }
        }.run {
            val exportProcess = try {
                kobwebGradle.export(siteLayout)
            }
            catch (ex: Exception) {
                exception = ex
                exportState = ExportState.INTERRUPTED
                return@run
            }
            exportProcess.consumeProcessOutput { line, isError ->
                handleGradleOutput(line, isError) { alert -> gradleAlertBundle.handleAlert(alert) }
            }

            onKeyPressed {
                if (exportState == ExportState.EXPORTING && key == Keys.Q) {
                    cancelReason = "User requested cancellation"
                    exportProcess.destroy()
                    exportState = ExportState.CANCELLING
                }
                else {
                    gradleAlertBundle.handleKey(key)
                }
            }

            while (exportProcess.isAlive) {
                delay(300)
            }

            if (exportProcess.exitValue() != 0) {
                if (exportState != ExportState.CANCELLING) {
                    cancelReason =
                        "Server failed to build. Please check Gradle output and fix the errors before continuing."
                    exportState = ExportState.CANCELLING
                }
            }

            if (exportState == ExportState.EXPORTING) {
                exportState = ExportState.FINISHING
            }
            check(exportState in listOf(ExportState.FINISHING, ExportState.CANCELLING))

            val stopProcess = kobwebGradle.stopServer()
            stopProcess.consumeProcessOutput(::handleConsoleOutput)
            stopProcess.waitFor()

            exportState = if (exportState == ExportState.FINISHING) ExportState.FINISHED else ExportState.CANCELLED
        }
    } else {
        assert(!isInteractive)
        assertKobwebApplication()
            .also { kobwebApplication -> kobwebApplication.assertServerNotAlreadyRunning() }

        kobwebGradle.export(siteLayout).also { it.consumeProcessOutput(); it.waitFor() }
        kobwebGradle.stopServer().also { it.consumeProcessOutput(); it.waitFor() }
    }
}