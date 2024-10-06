package de.gg.kruiser.lifecycle

import androidx.lifecycle.ViewModel
import de.gg.kruiser.core.BackstackItemId
import de.gg.kruiser.core.BackstackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Tracks orphans in a backstack, allowing to remove associated [ViewModel] or saved states when
 * they are no longer needed.
 */
interface OrphansTracker {
    fun track(item: BackstackItemId)
    fun untrack(item: BackstackItemId)
    fun untrack(items: List<BackstackItemId>)

    fun withOrphans(block: (orphans: List<BackstackItemId>) -> Unit)
}

internal class DefaultOrphansTracker(
    private val backstackState: BackstackState,
) : OrphansTracker {
    private val orphanCandidates = MutableStateFlow<List<BackstackItemId>>(emptyList())

    override fun track(item: BackstackItemId) = orphanCandidates.update { it + item }
    override fun untrack(item: BackstackItemId) = orphanCandidates.update { it - item }
    override fun untrack(items: List<BackstackItemId>) =
        orphanCandidates.update { it - items.toSet() }

    override fun withOrphans(block: (orphans: List<BackstackItemId>) -> Unit) =
        (orphanCandidates.value - backstackState.items.value.map { it.id }.toSet())
            .asReversed() // Clear in the same order as they were created
            .let(block)
}