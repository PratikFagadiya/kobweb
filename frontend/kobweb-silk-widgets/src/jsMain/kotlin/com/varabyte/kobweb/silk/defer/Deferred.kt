package com.varabyte.kobweb.silk.defer

import androidx.compose.runtime.*

internal class DeferredComposablesState {
    private val entries = mutableStateListOf<Entry>()

    fun append(): Entry = Entry().also {
        entries += it
    }

    @Composable
    fun forEach(render: @Composable (Entry) -> Unit) {
        // Copy the entries before enumerating, as the callback may, as a side effect, append more entries (for example,
        // a modal dialog triggering a tooltip).
        entries.toList().forEach { overlay ->
            render(overlay)
        }
    }

    inner class Entry {
        var content: (@Composable () -> Unit)? = null
        fun dismiss() {
            entries -= this
        }
    }
}

internal val LocalDeferred = staticCompositionLocalOf<DeferredComposablesState> {
    error("Attempting to defer rendering without calling `renderWithDeferred`, a required pre-requisite.")
}

/**
 * Defer the target [content] from rendering until the main content is finished.
 *
 * This has a nice side effect of de-parenting the target being rendered, potentially pulling it out of a deeply nested
 * hierarchy. This is particularly useful for overlays, like modals or tooltips, as they want to render separately (and
 * on top of everything) anyway. This also ensures the delayed render calls won't inherit any unexpected styles, like
 * opacity, or end up any parent elements with relative or absolution positions, messing with fullscreen calculations.
 */
@Composable
fun deferRender(content: @Composable () -> Unit) {
    val state = LocalDeferred.current
    val deferredEntry = remember(state) { state.append() }
    deferredEntry.content = content
    DisposableEffect(deferredEntry) { onDispose { deferredEntry.dismiss() }}
}

/**
 * Wraps a target composable with support for allowing deferred render calls.
 *
 * With this method called, any of the children Composables in [content] can trigger [deferRender], which will append
 * a render request which only gets run *after* the main content is finished rendering.
 *
 * You should only have to call this method once. Putting it near the root of your compose hierarchy is suggested.
 */
@Composable
fun renderWithDeferred(content: @Composable () -> Unit) {
    val state = DeferredComposablesState()
    CompositionLocalProvider(LocalDeferred provides state) {
        content()
        state.forEach { entry -> entry.content?.invoke() }
    }
}
