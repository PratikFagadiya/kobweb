// Add compose gradle plugin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.github.bitspittle"
version = "1.0-SNAPSHOT"

// Enable JS(IR) target and add dependencies
kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)

                implementation(project(":lib:core"))
                implementation(project(":lib:compose-ext"))
                implementation(project(":lib:ui"))
            }
        }
    }
}

enum class IconCategory {
    SOLID,
    REGULAR,
    BRAND,
}

tasks.register("regenerateIcons") {
    // {SOLID=[ad, address-book, address-card, ...], REGULAR=[address-book, address-card, angry, ...], ... }
    val iconRawNames = project.file("fa-icon-list.txt")
        .readLines().asSequence()
        .filter { line -> !line.startsWith("#") }
        .map { line ->
            // Convert icon name to function name, e.g.
            // align-left -> FaAlignLeft
            line.split("=", limit = 2).let { parts ->
                val category = when (parts[0]) {
                    "fas" -> IconCategory.SOLID
                    "far" -> IconCategory.REGULAR
                    "fab" -> IconCategory.BRAND
                    else -> throw GradleException("Unexpected category string: ${parts[0]}")
                }
                val names = parts[1]

                category to names.split(",")
            }
        }
        .toMap()

    // For each icon name, figure out what categories they are in. This will affect the function signature we generate.
    // {ad=[SOLID], address-book=[SOLID, REGULAR], address-card=[SOLID, REGULAR], ...
    val iconCategories = mutableMapOf<String, MutableSet<IconCategory>>()
    iconRawNames.forEach { entry ->
        val category = entry.key
        entry.value.forEach { rawName ->
            iconCategories.computeIfAbsent(rawName, { mutableSetOf() }).add(category)
        }
    }

    // Sanity check results
    iconCategories
        .filterNot { entry ->
            val categories = entry.value
            categories.size == 1 ||
                    (categories.size == 2 && categories.contains(IconCategory.SOLID) && categories.contains(IconCategory.REGULAR))
        }
        .let { invalidGroupings ->
            if (invalidGroupings.isNotEmpty()) {
                throw GradleException("Found unexpected groupings: $invalidGroupings")
            }
        }

    // Generate four types of functions: solid only, regular only, solid or regular, and brand
    val iconMethodEntries = iconCategories
        .map { entry ->
            val rawName = entry.key
            // Convert e.g. "align-items" to "FaAlignItems"
            val methodName = "Fa${rawName.split("-").joinToString("") { it.capitalize() }}"
            val categories = entry.value

            when {
                categories.size == 2 -> {
                    "@Composable fun $methodName(modifier: Modifier = Modifier, style: IconStyle = IconStyle.OUTLINE, color: Color = defaultColor) = FaIcon(\"$rawName\", modifier, style.category, color)"
                }
                categories.contains(IconCategory.SOLID) -> {
                    "@Composable fun $methodName(modifier: Modifier = Modifier, color: Color = defaultColor) = FaIcon(\"$rawName\", modifier, IconCategory.SOLID, color)"
                }
                categories.contains(IconCategory.REGULAR) -> {
                    "@Composable fun $methodName(modifier: Modifier = Modifier, color: Color = defaultColor) = FaIcon(\"$rawName\", modifier, IconCategory.REGULAR, color)"
                }
                categories.contains(IconCategory.BRAND) -> {
                    "@Composable fun $methodName(modifier: Modifier = Modifier, color: Color = defaultColor) = FaIcon(\"$rawName\", modifier, IconCategory.BRAND, color)"
                }
                else -> GradleException("Unhandled icon entry: $entry")
            }
        }

    val iconsCode = """
@file:Suppress("unused", "SpellCheckingInspection")

// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// THIS FILE IS AUTOGENERATED.
//
// Do not edit this file by hand. Instead, update `fa-icon-list` and run the Gradle task "regenerateIcons"
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

package kobweb.silk.components.icons.fa

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import kobweb.compose.ui.color
import kobweb.compose.ui.graphics.Color
import kobweb.silk.theme.SilkPallete
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.asAttributeBuilderApplier
import org.jetbrains.compose.web.dom.Div

enum class IconCategory(internal val className: String) {
    REGULAR("far"),
    SOLID("fas"),
    BRAND("fab");
}

enum class IconStyle(internal val category: IconCategory) {
    FILLED(IconCategory.SOLID),
    OUTLINE(IconCategory.REGULAR);
}

private val defaultColor: Color
    @Composable
    @ReadOnlyComposable
    get() = SilkPallete.current.onPrimary

@Composable
fun FaIcon(
    name: String,
    modifier: Modifier,
    style: IconCategory = IconCategory.REGULAR,
    color: Color = defaultColor
) {
    Div(
        attrs = modifier.color(color).castOrCreate().asAttributeBuilderApplier {
            classes(style.className, "fa-${'$'}name")
        }
    )
}

${iconMethodEntries.joinToString("\n")}
    """.trimIndent()

    println(project.file("src/jsMain/kotlin/kobweb/silk/components/icons/fa/FaIcons.kt").writeText(iconsCode))
}
