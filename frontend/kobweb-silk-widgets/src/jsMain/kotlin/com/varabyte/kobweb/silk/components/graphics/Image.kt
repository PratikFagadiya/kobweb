package com.varabyte.kobweb.silk.components.graphics

import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariantBase
import org.jetbrains.compose.web.css.*

// Note: The Silk `Image` widget itself is defined in the kobweb-silk module since it has dependencies on kobweb-core
// However, the styles are defined here, since this module is responsible for registering them, and it can still be
// useful to use them even without Kobweb.

val ImageStyle by ComponentStyle(prefix = "silk-") {}

val FitWidthImageVariant = ImageStyle.addVariantBase("fit") {
    Modifier
        .width(100.percent)
        .objectFit(ObjectFit.ScaleDown)
}
