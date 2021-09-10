package com.varabyte.kobweb.cli.create

import com.varabyte.kobweb.cli.create.freemarker.FreemarkerState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Metadata(
    val description: String? = null,
)


/**
 * Disable directory dot operations, e.g. "test/../../../../system"
 * to prevent template commands from escaping out of their root directories.
 */
private fun String.requireNoDirectoryDots() {
    require(this.split("/").none { it == "." || it == ".." })
}

/**
 * The base class for all instructions. Note that an instruction can be skipped if its condition evaluates to false.
 *
 * @param condition A value that should ultimately evaluate to "true" or "false". If "false", the instruction will be
 *   skipped. This value will be processed by freemarker and can be dynamic!
 */
@Serializable
sealed class Instruction(
    val condition: String? = null,
) {
    /**
     * Prompt the user to specify a value for a variable.
     *
     * @param name The name of this variable, which can be referenced in freemarker expressions later.
     * @param prompt The prompt to show the user.
     * @param default The default value to use if nothing is typed. This value will be processed by freemarker and can
     *   be dynamic!
     * @param validation One of a set of built in Kobweb validators. See [FreemarkerState.model] for the list.
     */
    @Serializable
    @SerialName("QueryVar")
    class QueryVar(
        val name: String,
        val prompt: String,
        val default: String? = null,
        val validation: String? = null,
    ) : Instruction()

    /**
     * Directly define a variable, useful if the user already defined another variable elsewhere and this is just a
     * minor modification to it.
     *
     * @param name The name of this variable, which can be referenced in freemarker expressions later.
     * @param value The value of the variable. This value will be processed by freemarker and can be dynamic!
     */
    @Serializable
    @SerialName("DefineVar")
    class DefineVar(
        val name: String,
        val value: String,
    ) : Instruction()

    /**
     * Search the project for all files that end in ".ftl", process them, and discard them.
     */
    @Serializable
    @SerialName("ProcessFreemarker")
    class ProcessFreemarker : Instruction()

    /**
     * Move files within the source folder.
     *
     * This can be a useful step to do before executing a [Keep] instruction later.
     *
     * @param from The files to copy. This can use standard wildcard syntax, e.g. "*.txt" and "a/b/**/README.md"
     * @param to The directory location to copy to. This value will be processed by freemarker and can be dynamic!
     * @param description An optional description to show to users, if set, instead of the default message, which
     *   may be too detailed.
     */
    @Serializable
    @SerialName("Move")
    class Move(
        val from: String,
        val to: String,
        val description: String? = null,
    ) : Instruction() {
        init {
            from.requireNoDirectoryDots()
            to.requireNoDirectoryDots()
        }
    }

    /**
     * Mark files as those that should be kept, i.e. copied out of the source template into the final destination
     * project. By default, this keeps everything except the kobweb.template.yaml file itself.
     *
     * @param files The list of files to keep
     * @param exclude Exceptions to the list of files to keep
     * @param description An optional description to show to users, if set, instead of the default message, which
     *   may be too detailed.
     */
    @Serializable
    @SerialName("Keep")
    class Keep(
        val files: String = "**",
        val exclude: String? = "kobweb.template.yaml",
        val description: String? = null,
    ) : Instruction() {
        init {
            files.requireNoDirectoryDots()
            exclude?.requireNoDirectoryDots()
        }
    }
}

@Serializable
class KobwebTemplate(
    val metadata: Metadata,
    val instructions: List<Instruction>,
)