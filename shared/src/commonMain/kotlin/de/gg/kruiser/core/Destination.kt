package de.gg.kruiser.core

import androidx.compose.runtime.Composable

interface Destination {
    fun renderer(): DestinationRenderer
}

fun Destination.defaultRenderer(content: @Composable () -> Unit): DestinationRenderer = object : DestinationRenderer {
    @Composable
    override fun Draw() {
        content()
    }
}

interface DestinationRenderer {
    @Composable
    fun Draw()
}
