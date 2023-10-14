package com.infosupport.ldoc.analyzerkt

import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.KtFile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.PrintStream

enum class State {
    INIT, OUTPUT, PROJECT
}

fun main(args: Array<String>) {
    var state = State.INIT
    var output = ""
    var project = ""
    var pretty = false

    for (arg in args) {
        when (arg) {
            "--output" -> state = State.OUTPUT
            "--project" -> state = State.PROJECT
            "--pretty" -> pretty = true
            else -> when (state) {
                State.INIT -> TODO()
                State.OUTPUT -> output = arg
                State.PROJECT -> project = arg
            }
        }
    }

    val om = ObjectMapper()
    val fos = FileOutputStream(File(output))
    var gen = om.createGenerator(fos)

    if (pretty) {
        gen = gen.useDefaultPrettyPrinter()
    }

    gen.writeStartArray()

    for (file in File(project).walk()) {
        if (file.isFile) {
            val configuration = CompilerConfiguration()
            configuration.put(
                    CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                    PrintingMessageCollector(PrintStream(OutputStream.nullOutputStream()), MessageRenderer.PLAIN_FULL_PATHS, true))

            val proj = KotlinCoreEnvironment.createForProduction(
                    Disposer.newDisposable(),
                    configuration,
                    EnvironmentConfigFiles.JVM_CONFIG_FILES).project

            val psim = PsiManager.getInstance(proj)

            val ktfile = psim.findFile(LightVirtualFile(file.name, file.readText())) as KtFile

            ktfile.accept(AnalysisVisitor(gen))
        }
    }

    gen.writeEndArray()
    gen.close()
}
