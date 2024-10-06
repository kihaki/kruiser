package de.gg.kruiser.lifecycle.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.gg.kruiser.core.BackstackState
import de.gg.kruiser.lifecycle.DefaultOrphansTracker
import de.gg.kruiser.lifecycle.OrphansTrackerHolder

@Composable
fun rememberOrphansTracker(state: BackstackState) = remember(state) {
    OrphansTrackerHolder().getOrPut(state.id) { DefaultOrphansTracker(state) }
}