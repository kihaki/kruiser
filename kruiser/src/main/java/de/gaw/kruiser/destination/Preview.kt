package de.gaw.kruiser.destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.gaw.kruiser.backstack.core.BackstackEntries
import de.gaw.kruiser.backstack.core.BackstackEntry
import de.gaw.kruiser.backstack.core.MutableBackstackState
import de.gaw.kruiser.backstack.ui.BackstackContext
import de.gaw.kruiser.backstack.ui.rendering.LocalBackstackEntry
import de.gaw.kruiser.backstack.ui.rendering.Render
import de.gaw.kruiser.backstack.ui.util.currentOrThrow

/**
 * Renders a preview of a destination, intended for Editor previews.
 */
@Composable
fun Destination.Preview(
    entriesBefore: BackstackEntries = emptyList(),
) {
    val previewBackstackState = remember(this) {
        MutableBackstackState(
            "preview-backstack-state",
            entriesBefore + BackstackEntry(
                destination = this,
                id = "preview-current-backstack-entry-id", // stable id for previews
            ),
        )
    }
    BackstackContext(
        backstackState = previewBackstackState,
    ) {
        val currentBackstackEntry = LocalBackstackEntry.currentOrThrow
        currentBackstackEntry.Render()
    }
}