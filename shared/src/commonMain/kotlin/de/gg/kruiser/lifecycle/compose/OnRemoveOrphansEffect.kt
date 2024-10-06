package de.gg.kruiser.lifecycle.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import de.gg.kruiser.core.BackstackItemId
import de.gg.kruiser.lifecycle.OrphansTracker

/**
 * Triggers [onRemove] for all orphans (tracked items that are not on the backstack anymore),
 * when this composable leaves the composition.
 *
 * Remember to track orphans with [OrphansTracker.track] too!
 */
@Composable
internal fun OnRemoveOrphansEffect(
    orphansTracker: OrphansTracker,
    onRemove: (orphan: BackstackItemId) -> Unit,
) {
    DisposableEffect(orphansTracker) {
        onDispose {
            orphansTracker.withOrphans { orphans ->
                orphans.forEach(onRemove)
                orphansTracker.untrack(orphans)
            }
        }
    }
}